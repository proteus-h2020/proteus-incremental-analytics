package com.treelogic.proteus.flink.incops;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import com.treelogic.proteus.flink.incops.config.IncrementalConfiguration;
import com.treelogic.proteus.flink.incops.config.OpParameter;
import com.treelogic.proteus.flink.incops.states.DataSerie;
import com.treelogic.proteus.flink.incops.states.IncResult;
import com.treelogic.proteus.flink.incops.util.Stateful;
import com.treelogic.proteus.flink.utils.FieldUtils;

public abstract class IncrementalOperation<IN, OUT extends Stateful<Double>>
		extends RichWindowFunction<IN, IncResult<OUT>, Tuple, GlobalWindow> {

	protected ValueStateDescriptor<Map<String, OUT>> stateDescriptor;

	protected abstract int numberOfRequiredDataSeries();

	private boolean isStateInitialized;

	private static final long serialVersionUID = 1L;

	protected IncrementalConfiguration configuration;

	protected abstract void updateWindow(String field, List<DataSerie> dataSeries, OUT status);

	protected Log log = LogFactory.getLog(this.getClass());

	private Stateful<?> stateful;

	public IncrementalOperation(IncrementalConfiguration configuration, Stateful<?> stateful) {
		this.configuration = configuration;
		this.stateful = stateful;

		if (configuration.getFields() == null || configuration.getFields().length < 1) {
			throw new IllegalArgumentException("Illegal number of fields");
		}
		initializeDescriptor();
	}

	@Override
	public void apply(Tuple key, GlobalWindow window, Iterable<IN> input, Collector<IncResult<OUT>> out)
			throws Exception {
		if (!isStateInitialized) {
			createState();
			isStateInitialized = true;
		}
		OpParameter[] parameters = this.configuration.getFields();
		ValueState<Map<String, OUT>> state = getRuntimeContext().getState(stateDescriptor);
		Map<String, OUT> stateValues = state.value();
		MultiValueMap values = new MultiValueMap();

		// Loop values & fields
		for (IN in : input) {
			for (OpParameter parameter : parameters) {
				for (String field : parameter.getFields()) {
					Double value = FieldUtils.getValue(in, field);
					values.put(field, value);
				}
			}
		}

		applyOperation(values, stateValues);
		state.update(stateValues);

		IncResult<OUT> result = new IncResult<OUT>();

		for (Entry<String, OUT> entry : stateValues.entrySet()) {
			result.put(entry.getKey(), key.toString(), entry.getValue());
		}

		out.collect(result);

	}

	@SuppressWarnings("unchecked")
	protected void applyOperation(MultiValueMap values, Map<String, OUT> states) {
		OpParameter[] parameters = this.configuration.getFields();
		List<DataSerie> dataSeries = new ArrayList<DataSerie>();

		for (OpParameter parameter : parameters) {
			dataSeries.clear();
			for (String field : parameter.getFields()) {
				DataSerie serie = new DataSerie().field(field).values((List<Double>) values.get(field));
				dataSeries.add(serie);
			}
			OUT state = states.get(parameter.getComposedName());
			if(assertWindowRestriction(dataSeries)){
				updateWindow(parameter.getComposedName(), dataSeries, state);
			}
		}

	}

	private boolean assertWindowRestriction(List<DataSerie> series) {
		if (series.size() != numberOfRequiredDataSeries()) {
			throw new IllegalStateException(this.getClass().getSimpleName() + " should receive just "
					+ numberOfRequiredDataSeries() + " data Serie. Instead: " + series.size());
		}
		return true;
	}

	private void initializeDescriptor() {
		stateDescriptor = new ValueStateDescriptor<>("last-result", new TypeHint<Map<String, OUT>>() {
		}.getTypeInfo(), new HashMap<String, OUT>());
	}

	@SuppressWarnings("unchecked")
	private void createState() {
		ValueState<Map<String, OUT>> state = getRuntimeContext().getState(stateDescriptor);
		Map<String, OUT> stateValues = null;
		try {
			stateValues = state.value();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		OpParameter[] opParameters = this.configuration.getFields();

		Class<OUT> clazz = (Class<OUT>) stateful.getClass();
		for (OpParameter field : opParameters) {
			try {
				stateValues.put(field.getComposedName(), clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		try {
			state.update(stateValues);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}