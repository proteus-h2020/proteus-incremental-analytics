package com.treelogic.proteus.flink.incops;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import com.treelogic.proteus.flink.incops.util.StatefulVariance;

/**
 * Incremental variance using the general updating formula described in
 * http://i.stanford.edu/pub/cstr/reports/cs/tr/79/773/CS-TR-79-773.pdf
 *
 * @param <IN> Pojo type that contains the field to be analysed
 */
public class IncrementalVariance<IN>
    extends IncrementalOperation<IN, Double> {

    private static final long serialVersionUID = 1L;
    private final String field;

    private ValueStateDescriptor<StatefulVariance> descriptor;

    public IncrementalVariance(String field) {
        if (field == null || field.equals("")) {
            throw new IllegalArgumentException("Field cannot be empty");
        }

        this.field = field;

        descriptor = new ValueStateDescriptor<StatefulVariance>(
            "incremental-variance-descriptor",
            TypeInformation.of(new TypeHint<StatefulVariance>() {}),
            new StatefulVariance());
    }

    @Override
    public void apply(Tuple key,
                      GlobalWindow window,
                      Iterable<IN> input,
                      Collector<Double> out) throws Exception {

        StatefulVariance state = getRuntimeContext().getState(descriptor).value();

        // TODO Set initial size equals to window size?
        List<Double> elems = new ArrayList<>();

        for (IN in : input) {
            Field field = in.getClass().getDeclaredField(this.field);
            field.setAccessible(true);
            elems.add((Double) field.get(in));
        }

        double result = state.apply(elems);
        getRuntimeContext().getState(descriptor).update(state);
        out.collect(result);
    }
}
