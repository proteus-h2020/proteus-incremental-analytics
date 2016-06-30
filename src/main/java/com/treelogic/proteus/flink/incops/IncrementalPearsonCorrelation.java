package com.treelogic.proteus.flink.incops;

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
import com.treelogic.proteus.flink.incops.entities.IncResult;
import com.treelogic.proteus.flink.incops.util.StatefulAverage;
import com.treelogic.proteus.flink.incops.util.StatefulPearsonCorrelation;
import com.treelogic.proteus.flink.utils.FieldUtils;

/**
 * Incremental implementation of the Pearson product-moment correlation coefficient
 *
 * @param <IN>
 */
public class IncrementalPearsonCorrelation<IN>
		extends IncrementalOperation<IN, StatefulPearsonCorrelation> {

	private static final long serialVersionUID = 1L;	
	private ValueStateDescriptor<Map<String, StatefulPearsonCorrelation>> descriptor;
	
	public IncrementalPearsonCorrelation(IncrementalConfiguration configuration) {
		super(configuration);
	}

	@Override
	public void apply(Tuple key, GlobalWindow window, Iterable<IN> input,
			Collector<IncResult<StatefulPearsonCorrelation>> out) throws Exception {

		ValueState<Map<String, StatefulPearsonCorrelation>> state = getRuntimeContext().getState(descriptor);
		
		Map<String, StatefulPearsonCorrelation> pearsons = state.value();

		// TODO Set initial size equals to window size?
		//List<Double> xElems = new ArrayList<>(), yElems = new ArrayList<>();
		Map<String, List<Double>> xElemnsMap = new HashMap<String, List<Double>>();
		Map<String, List<Double>> yElemnsMap = new HashMap<String, List<Double>>();
		
		for(IN in : input){
			for(String field : this.configuration.getFields()){
				List<Double> xElems = xElemnsMap.get(field);
				if (xElems == null) {
					xElems = new ArrayList<Double>();
					xElemnsMap.put(field, xElems);
				}
				List<Double> yElems = yElemnsMap.get(field);
				if (yElems == null) {
					yElems = new ArrayList<Double>();
					yElemnsMap.put(field, yElems);
				}

				String[] fNames = field.split(",");
				
				Double xValue = FieldUtils.getValue(in, fNames[0]);
				Double yValue = FieldUtils.getValue(in, fNames[1]);

				xElems.add(xValue);
				yElems.add(yValue);
				if (pearsons.get(field) == null) {
					pearsons.put(field, new StatefulPearsonCorrelation());
				}
				
			}
		}
		IncResult<StatefulPearsonCorrelation> result = new IncResult<>();
		//Map<String, Tuple2<String, Double>> tupleMap = new HashMap<String, Tuple2<String, Double>>();
		for (Entry<String, StatefulPearsonCorrelation> entry : pearsons.entrySet()) {
			entry.getValue().apply(xElemnsMap.get(entry.getKey()), yElemnsMap.get(entry.getKey()));
 			//System.out.println(result);
			//double result = entry.getValue().apply(xElemnsMap.get(entry.getKey()), yElemnsMap.get(entry.getKey()));
			//tupleMap.put(entry.getKey(), new Tuple2<String, Double>(key.toString(), result));
		}
		state.update(pearsons);
		out.collect(result);
	}

	@Override
	public void initializeDescriptor() {
		descriptor = new ValueStateDescriptor<Map<String, StatefulPearsonCorrelation>>(
				"incremental-pearson-correlation-descriptor",
				TypeInformation.of(new TypeHint<Map<String, StatefulPearsonCorrelation>>() {}),
				new HashMap<String, StatefulPearsonCorrelation>());
	}		

}
