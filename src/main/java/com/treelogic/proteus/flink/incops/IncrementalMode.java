package com.treelogic.proteus.flink.incops;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Operator that incrementally computes the mode of a variable. State is
 * saved using Flink's key/value store and a java.util.HashMap object. The
 * key of the map represents variable's value and the value represents the
 * repetitions of that value.
 *
 * @param <IN> POJO that contains fields to be analyzed
 */
public class IncrementalMode<IN> extends
  RichWindowFunction<IN, List<Tuple2<Double, Integer>>, Tuple, GlobalWindow> {

    private static final long serialVersionUID = 1;

    private ValueStateDescriptor<Map<Double, Integer>>
      stateDescriptor;

    private String field = "";
    private int numValues, numDecimals;

    public IncrementalMode(String field) {
        this(field, 1, 0);
    }

    public IncrementalMode(String field, int numValues, int numDecimals) {
        if (field == null || field.equals("")) {
            throw new IllegalArgumentException("Field cannot be empty");
        }

        this.field = field;
        this.numValues = numValues > 0 ? numValues : 1;
        this.numDecimals = numDecimals >= 0 ? numDecimals : 0;

        stateDescriptor = new ValueStateDescriptor<Map<Double, Integer>>(
          "mode-last-result",
          //new TypeHint<Map<Double, Integer>>() {}.getTypeInfo(),
          TypeInformation.of(new TypeHint<Map<Double, Integer>>() {
          }),
          new HashMap<Double, Integer>());
    }

    public void apply(Tuple key, GlobalWindow window, Iterable<IN> input,
                      Collector<List<Tuple2<Double, Integer>>> out) throws Exception {
        ValueState<Map<Double, Integer>> state =
          getRuntimeContext().getState(stateDescriptor);

        List<Tuple2<Double, Integer>> list = new ArrayList<>(numValues);
        Map<Double, Integer> lastRecord = state.value();

        // Update map
        for (IN in : input) {
            Field field = in.getClass().getDeclaredField(this.field);
            field.setAccessible(true);

            double pow = Math.pow(10, numDecimals);
            Double i = Math.round((Double) field.get(in) * pow) / pow;

            if (!lastRecord.containsKey(i)) {
                lastRecord.put(i, 1);
            } else {
                lastRecord.put(i, lastRecord.get(i) + 1);
            }
        }

        // Update state
        state.update(lastRecord);

        // Find the new modes
        TreeSet<Tuple2<Double, Integer>> set = toTreeSet(lastRecord);
        Iterator<Tuple2<Double, Integer>> it = set.iterator();
        int i = 0;

        while (it.hasNext() && i < numValues) {
            list.add(it.next());
            i++;
        }

        // Return the mode element and the value of it's mode
        out.collect(list);
    }

    private TreeSet<Tuple2<Double, Integer>> toTreeSet(Map<Double, Integer> lastRecord) {
        // Order elements by number of appearances
        Comparator<Tuple2<Double, Integer>> c =
          new Comparator<Tuple2<Double, Integer>>() {
              public int compare(Tuple2<Double, Integer> o1, Tuple2<Double, Integer> o2) {
                  return o1.f1 < o2.f1 ? 1 : -1;
              }
          };

        TreeSet<Tuple2<Double, Integer>> set = new TreeSet<>(c);

        for (Map.Entry<Double, Integer> e : lastRecord.entrySet()) {
            set.add(new Tuple2<Double, Integer>(e.getKey(), e.getValue()));
        }

        return set;
    }
}
