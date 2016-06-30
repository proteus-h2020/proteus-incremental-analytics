package com.treelogic.proteus.flink.incops;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.treelogic.proteus.flink.incops.entities.IncResult;
import com.treelogic.proteus.flink.incops.util.Stateful;
import com.treelogic.proteus.flink.utils.FieldUtils;

public abstract class IncrementalOperation<IN, OUT extends Stateful<Number>>
		extends RichWindowFunction<IN, IncResult<OUT>, Tuple, GlobalWindow> {

	protected ValueStateDescriptor<Map<String, OUT>> stateDescriptor;

	private boolean isStateInitialize;

	private static final long serialVersionUID = 1L;
	
	private static final String ERROR = "Field cannot neither be null nor empty";

	protected IncrementalConfiguration configuration;
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	private Stateful<?> stateful;

	protected abstract void updateWindow(String field, List<Number> numbers, OUT status);

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
		if (!isStateInitialize) {
			createState();
			isStateInitialize = true;
		}
		String[] fields = this.configuration.getFields();
		ValueState<Map<String, OUT>> state = getRuntimeContext().getState(stateDescriptor);
		Map<String, OUT> stateValues = state.value();
		MultiValueMap values = new MultiValueMap();

		// Loop values & fields
		for (IN in : input) {
			for (String fName : fields) {
				Double value = FieldUtils.getValue(in, fName);
				values.put(fName, value);
			}
		}

		applyOperation(values, stateValues);
		state.update(stateValues);

	}

	@SuppressWarnings("unchecked")
	protected void applyOperation(MultiValueMap values, Map<String, OUT> states) {
		for (Object field : values.keySet()) {
			List<Number> numbers = (List<Number>) values.get(field);
			OUT state = states.get(field);
			updateWindow(field.toString(), numbers, state);
		}
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
		String[] fields = this.configuration.getFields();

		Class<OUT> clazz = (Class<OUT>) stateful.getClass();
		for (String field : fields) {
			try {
				stateValues.put(field, clazz.newInstance());
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
