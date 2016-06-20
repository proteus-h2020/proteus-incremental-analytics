package com.treelogic.proteus.flink.incops;

import java.lang.reflect.Field;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import com.treelogic.proteus.flink.incops.util.MeanTuple;

public class IncrementalAverage<IN>
		extends IncrementalOperation<IN, Tuple2<String, Double>> {

	private static final long serialVersionUID = 1L;

	// public static final String OP_NAME = "avg";

	private String field;
	private ValueStateDescriptor<MeanTuple> stateDescriptor;

	public IncrementalAverage(String field) {
		if (field == null || field.equals("")) {
			throw new IllegalArgumentException("Field cannot be empty");
		}

		this.field = field;

		stateDescriptor = new ValueStateDescriptor<>(
			"last-result",
			new TypeHint<MeanTuple>() {}.getTypeInfo(),
			new MeanTuple());

	}

	@Override
	public void apply(Tuple key,
			GlobalWindow window,
			Iterable<IN> input,
			Collector<Tuple2<String, Double>> collector) throws Exception {
		
		// Get last window interaction values
		ValueState<MeanTuple> state = getRuntimeContext().getState(stateDescriptor);
		MeanTuple lastRegister = state.value();

		for (IN in : input) {
			Field field = in.getClass().getDeclaredField(this.field);
			field.setAccessible(true);

			Double value = (Double) field.get(in);
			lastRegister.inc(value);
		}

		// Update status
		state.update(lastRegister);

		collector.collect(new Tuple2<>(key.toString(), lastRegister.mean()));
	}
}
