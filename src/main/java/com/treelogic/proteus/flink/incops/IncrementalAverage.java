package com.treelogic.proteus.flink.incops;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import com.treelogic.proteus.flink.incops.config.IncrementalConfiguration;
import com.treelogic.proteus.flink.incops.util.MeanTuple;
import com.treelogic.proteus.flink.utils.FieldUtils;

public class IncrementalAverage<IN> extends IncrementalOperation<IN, Map<String, Tuple2<String, Double>>> {

	private static final long serialVersionUID = 1L;
	private ValueStateDescriptor<Map<String, MeanTuple>> stateDescriptor;

	public IncrementalAverage(IncrementalConfiguration configuration) {
		super(configuration);
	}

	@Override
	public void apply(Tuple key, GlobalWindow window, Iterable<IN> input,
			Collector<Map<String, Tuple2<String, Double>>> collector) throws Exception {

		// Get last window interaction values
		ValueState<Map<String, MeanTuple>> state = getRuntimeContext().getState(stateDescriptor);
		Map<String, MeanTuple> meanTuples = state.value();

		String[] fields = this.configuration.getFields();

		for (IN in : input) {
			for (String fName : fields) {
				MeanTuple meanTuple = meanTuples.get(fName);
				if (meanTuple == null) {
					meanTuple = new MeanTuple();
					meanTuples.put(fName, meanTuple);
				}
				Double value = FieldUtils.getValue(in, fName);
				meanTuple.inc(value);
			}
		}
		// Update status
		state.update(meanTuples);

		Map<String, Tuple2<String, Double>> tupleMap = new HashMap<String, Tuple2<String, Double>>();
		for (Entry<String, MeanTuple> entry : meanTuples.entrySet()) {
			tupleMap.put(entry.getKey(), new Tuple2<String, Double>(key.toString(), entry.getValue().mean()));
		}

		collector.collect(tupleMap);
	}

	@Override
	public void initializeDescriptor() {
		stateDescriptor = new ValueStateDescriptor<>("last-result", new TypeHint<Map<String, MeanTuple>>() {
		}.getTypeInfo(), new HashMap<String, MeanTuple>());
	}
}
