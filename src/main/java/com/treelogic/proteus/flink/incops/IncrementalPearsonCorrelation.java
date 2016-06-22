package com.treelogic.proteus.flink.incops;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import com.treelogic.proteus.flink.incops.util.StatefulPearsonCorrelation;

/**
 * Incremental implementation of the Pearson product-moment correlation coefficient
 *
 * @param <IN>
 */
public class IncrementalPearsonCorrelation<IN>
		extends IncrementalOperation<IN, Tuple2<String, Double>> {

	private static final long serialVersionUID = 1L;

	private final String fieldX, fieldY;
	
	private ValueStateDescriptor<StatefulPearsonCorrelation> descriptor;

	public IncrementalPearsonCorrelation(String fieldX, String fieldY) {
		checkFields(new String[]{fieldX, fieldY});

		this.fieldX = fieldX;
		this.fieldY = fieldY;

		descriptor = new ValueStateDescriptor<StatefulPearsonCorrelation>(
				"incremental-pearson-correlation-descriptor",
				TypeInformation.of(new TypeHint<StatefulPearsonCorrelation>() {}),
				new StatefulPearsonCorrelation());
	}

	@Override
	public void apply(Tuple key, GlobalWindow window, Iterable<IN> input,
			Collector<Tuple2<String, Double>> out) throws Exception {

		StatefulPearsonCorrelation state = getRuntimeContext().getState(descriptor).value();
		
		// TODO Set initial size equals to window size?
		List<Double> xElems = new ArrayList<>(), yElems = new ArrayList<>();

		for (IN in : input) {
			Field fieldX = in.getClass().getDeclaredField(this.fieldX);
			Field fieldY = in.getClass().getDeclaredField(this.fieldY);
			fieldX.setAccessible(true);
			fieldY.setAccessible(true);
			xElems.add((Double) fieldX.get(in));
			yElems.add((Double) fieldY.get(in));
		}

		double result = state.apply(xElems, yElems);
		getRuntimeContext().getState(descriptor).update(state);
		out.collect(new Tuple2<>(key.toString(), result));
	}

}
