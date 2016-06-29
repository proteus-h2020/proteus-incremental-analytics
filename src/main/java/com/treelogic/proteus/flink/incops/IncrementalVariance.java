package com.treelogic.proteus.flink.incops;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import com.treelogic.proteus.flink.incops.config.IncrementalConfiguration;
import com.treelogic.proteus.flink.incops.util.StatefulVariance;
import com.treelogic.proteus.flink.utils.FieldUtils;

/**
 * Incremental variance using the general updating formula described in
 * http://i.stanford.edu/pub/cstr/reports/cs/tr/79/773/CS-TR-79-773.pdf
 *
 * @param <IN>
 *            Pojo type that contains the field to be analysed
 */
public class IncrementalVariance<IN> extends IncrementalOperation<IN, Map<String, Tuple2<String, Double>>> {

	private static final long serialVersionUID = 1L;
	// private final String field;

	private ValueStateDescriptor<Map<String, StatefulVariance>> descriptor;

	public IncrementalVariance(IncrementalConfiguration configuration) {
		super(configuration);

	}

	@Override
	public void apply(Tuple key, GlobalWindow window, Iterable<IN> input,
			Collector<Map<String, Tuple2<String, Double>>> out) throws Exception {

		ValueState<Map<String, StatefulVariance>> state = getRuntimeContext().getState(descriptor);

		Map<String, StatefulVariance> variances = state.value();
		Map<String, List<Double>> elemsMap = new HashMap<String, List<Double>>();

		// TODO Set initial size equals to window size?

		for (IN in : input) {
			for (String fName : this.configuration.getFields()) {
				List<Double> elems = elemsMap.get(fName);
				if (elems == null) {
					elems = new ArrayList<Double>();
					elemsMap.put(fName, elems);
				}
				Double value = FieldUtils.getValue(in, fName);
				elems.add(value);

				if (variances.get(fName) == null) {
					variances.put(fName, new StatefulVariance());
				}
			}
		}

		Map<String, Tuple2<String, Double>> tupleMap = new HashMap<String, Tuple2<String, Double>>();

		for (Entry<String, StatefulVariance> entry : variances.entrySet()) {
			double result = entry.getValue().apply(elemsMap.get(entry.getKey()));
			tupleMap.put(entry.getKey(), new Tuple2<String, Double>(key.toString(), result));
		}
		state.update(variances);
		out.collect(tupleMap);
	}

	@Override
	public void initializeDescriptor() {
		descriptor = new ValueStateDescriptor<Map<String, StatefulVariance>>("incremental-variance-descriptor",
				TypeInformation.of(new TypeHint<Map<String, StatefulVariance>>() {
				}), new HashMap<String, StatefulVariance>());
	}
}
