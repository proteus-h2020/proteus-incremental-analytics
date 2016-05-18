package com.treelogic.proteus.flink.incops;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IncrementalVariance<IN>
    extends RichWindowFunction<IN, Double, Tuple, GlobalWindow> {

    private static final long serialVersionUID = 1L;
    private final String field;

    private ListStateDescriptor<List<Double>> descriptor;

    public IncrementalVariance(String field) {
        if (field == null || field.equals("")) {
            throw new IllegalArgumentException("Field cannot be empty");
        }

        this.field = field;

        descriptor = new ListStateDescriptor<>(
            "incremental-variance-descriptor",
            TypeInformation.of(new TypeHint<List<Double>>() {}));
    }

    public void apply(Tuple key,
                      GlobalWindow window,
                      Iterable<IN> input,
                      Collector<Double> out) throws Exception {

        ListState<List<Double>> state =
            getRuntimeContext().getListState(descriptor);

        List<List<Double>> previous = toList(state.get());

        // TODO Set initial size equals to window size?
        List<Double> elems = new ArrayList<>();

        for (IN in : input) {
            Field field = in.getClass().getDeclaredField(this.field);
            field.setAccessible(true);
            elems.add((Double) field.get(in));
        }

        Double mean = mean(elems);
        previous.add(elems);
        Double grantMean = grantMean(previous, mean);

        // Necesito acceder a todos los elementos de todas las ventanas...
        double ess = 0;
        int n = 0;
        for (List<Double> l : previous) {
            for (Double d : l) {
                n += 1;
                ess += Math.pow(d - grantMean, 2);
            }
        }

        state.add(elems);

        out.collect(ess / n);
    }

    private Double grantMean(List<List<Double>> elems, Double mean) {
        double sum = 0;
        int n = 0;

        for (List<Double> elem : elems) {
            for (Double d : elem) {
                sum += d;
                n += 1;
            }
        }

        return sum / n;
    }

    private List<List<Double>> toList(
        Iterable<List<Double>> iterable) {

        Iterator<List<Double>> it = iterable.iterator();
        List<List<Double>> list = new ArrayList<>();

        while (it.hasNext()) {
            list.add(it.next());
        }

        return list;
    }

    private Double mean(List<Double> elems) {
        double sum = 0;

        for (Double d : elems) {
            sum += d;
        }

        return sum / elems.size();
    }
}
