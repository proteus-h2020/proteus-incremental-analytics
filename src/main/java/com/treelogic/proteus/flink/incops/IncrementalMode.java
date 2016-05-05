package com.treelogic.proteus.flink.incops;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Operator that incrementally computes the mode of a variable. State is
 * saved using Flink's key/value store and a java.util.HashMap object. The
 * key of the map represents variable's value and the value represents the
 * repetitions of that value.
 *
 * @param <IN> POJO that contains fields to be analyzed
 *
 */
public class IncrementalMode<IN> extends
  RichWindowFunction<IN,Tuple2<Double, Integer>, Tuple, GlobalWindow> {

    private static final long serialVersionUID = 1;

    private ValueStateDescriptor<Map<Double, Integer>>
      stateDescriptor;

    private String field = "";

    /**
     * Constructs an incremental mode operator that computes the mode using
     * the values of field
     * @param field Field whose values will be analyzed to compute the mode
     */
    public IncrementalMode(String field) {
        if (field == null || field.equals("")) {
            throw new IllegalArgumentException("Field cannot be empty");
        }

        this.field = field;

        stateDescriptor = new ValueStateDescriptor<Map<Double, Integer>>
          ("mode-last-result",
          new TypeHint<Map<Double, Integer>>() {}.getTypeInfo(),
          new HashMap<Double, Integer>());
    }

    public void apply(Tuple key, GlobalWindow window, Iterable<IN> input,
                      Collector<Tuple2<Double, Integer>> out) throws Exception {
        ValueState<Map<Double, Integer>> state =
          getRuntimeContext().getState(stateDescriptor);

        Map<Double, Integer> lastRecord = state.value();

        // Update map
        for(IN in : input) {
            Field field = in.getClass().getDeclaredField(this.field);
            field.setAccessible(true);
            Double i = (Double) field.get(in);

            if(!lastRecord.containsKey(i)) {
                lastRecord.put(i, 1);
            } else {
                lastRecord.put(i, lastRecord.get(i) + 1);
            }
        }

        // Find the new mode
        Map.Entry<Double, Integer> max = null;

        for(Map.Entry<Double, Integer> e : lastRecord.entrySet()) {
            if(max == null || e.getValue().compareTo(max.getValue()) > 0) {
                max = e;
            }
        }

        // Update state
        state.update(lastRecord);

        out.collect(new Tuple2<Double, Integer>(max.getKey(), max.getValue()));
    }
}
