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
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import com.treelogic.proteus.flink.incops.config.IncrementalConfiguration;
import com.treelogic.proteus.flink.incops.entities.IncResult;
import com.treelogic.proteus.flink.incops.util.Stateful;
import com.treelogic.proteus.flink.incops.util.StatefulCovariance;
import com.treelogic.proteus.flink.utils.FieldUtils;

/**
 * Incremental covariance using the updating formula described in
 * http://prod.sandia.gov/techlib/access-control.cgi/2008/086212.pdf
 *
 * @param <IN>
 *            Pojo type that contains the field to be analysed
 */
public class IncrementalCovariance<IN> extends IncrementalOperation<IN, StatefulCovariance> {


	private static final long serialVersionUID = 1L;

	public IncrementalCovariance(IncrementalConfiguration configuration) {
		super(configuration, new StatefulCovariance());
	}

	@Override
	protected void updateWindow(String field, List<Number> numbers, StatefulCovariance status) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	
	
	/**
	private static final long serialVersionUID = 1L;
	private ValueStateDescriptor<Map<String, StatefulCovariance>> descriptor;

	public IncrementalCovariance(IncrementalConfiguration configuration) {
		super(configuration);

	}

	@Override
	public void apply(Tuple key, GlobalWindow window, Iterable<IN> input,
			Collector<IncResult<StatefulCovariance>> collector) throws Exception {

		ValueState<Map<String, StatefulCovariance>> state = getRuntimeContext().getState(descriptor);

		Map<String, StatefulCovariance> covariances = state.value();

		// TODO Set initial size equals to window size?
		Map<String, List<Double>> xElemnsMap = new HashMap<String, List<Double>>();
		Map<String, List<Double>> yElemnsMap = new HashMap<String, List<Double>>();

		for (IN in : input) {
			for (String field : this.configuration.getFields()) {
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

				if (covariances.get(field) == null) {
					covariances.put(field, new StatefulCovariance());
				}
			}
		}

		IncResult<StatefulCovariance> result = new IncResult<StatefulCovariance>();

		for (Entry<String, StatefulCovariance> entry : covariances.entrySet()) {
			StatefulCovariance status = entry.getValue();
			status.apply(xElemnsMap.get(entry.getKey()), yElemnsMap.get(entry.getKey()));
			result.put(entry.getKey(), key.toString(), status);

		}

		state.update(covariances);

		collector.collect(result);
	}

	@Override
	public void initializeDescriptor() {
		descriptor = new ValueStateDescriptor<>("incremental-covariance-descriptor",
				TypeInformation.of(new TypeHint<Map<String, StatefulCovariance>>() {
				}), new HashMap<String, StatefulCovariance>());
	}
	**/
}
