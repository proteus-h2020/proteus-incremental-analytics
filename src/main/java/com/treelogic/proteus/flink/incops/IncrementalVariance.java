package com.treelogic.proteus.flink.incops;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Incremental variance using the general updating formula described in
 * http://i.stanford.edu/pub/cstr/reports/cs/tr/79/773/CS-TR-79-773.pdf
 *
 * @param <IN> Pojo type that contains the field to be analysed
 */
public class IncrementalVariance<IN>
    extends RichWindowFunction<IN, Double, Tuple, GlobalWindow> {

    private static final long serialVersionUID = 1L;
    private final String field;

    /**
     * Tuple3 - SumSquaredErrors, SumElems, ProcessedWindowsCount>
     */
    private ValueStateDescriptor<Tuple3<Double, Double, Integer>> descriptor;

    public IncrementalVariance(String field) {
        if (field == null || field.equals("")) {
            throw new IllegalArgumentException("Field cannot be empty");
        }

        this.field = field;

        descriptor = new ValueStateDescriptor<Tuple3<Double, Double, Integer>>(
            "incremental-variance-descriptor",
            TypeInformation.of(new TypeHint<Tuple3<Double, Double, Integer>>() {}),
            new Tuple3<Double, Double, Integer>(0d, 0d, 0));
    }

    public void apply(Tuple key,
                      GlobalWindow window,
                      Iterable<IN> input,
                      Collector<Double> out) throws Exception {

        ValueState<Tuple3<Double, Double, Integer>> state =
            getRuntimeContext().getState(descriptor);

        // TODO Set initial size equals to window size?
        List<Double> elems = new ArrayList<>();

        for (IN in : input) {
            Field field = in.getClass().getDeclaredField(this.field);
            field.setAccessible(true);
            elems.add((Double) field.get(in));
        }

        // Values from last window
        double S = state.value().f0, T = state.value().f1;
        int j = state.value().f2 + 1;

        //Values for this window, currentS and currentT
        double cS = 0, cT = elems.get(0);

        for (int i = 2; i <= elems.size(); i++) {
            double last = elems.get(i - 1);
            cT += last;
            double num = Math.pow((i * last) - cT, 2);
            double denom = (double) i * (i - 1);
            cS = cS + (num / denom);
        }

        int size = elems.size();

        if (j == 1) {
            state.update(new Tuple3<Double, Double, Integer>(cS, cT, j));
            out.collect(cS / (size - 1));
        } else {
            double pow, f, St;
            double m = (j - 1) * size, n = size;

            pow = Math.pow(((n / m) * T) - cT, 2);
            f = m / (n * (m + n));
            St = S + cS + (f * pow);
            state.update(new Tuple3<Double, Double, Integer>(St, T + cT, j));
            out.collect(St / (m + n - 1));
        }
    }
}
