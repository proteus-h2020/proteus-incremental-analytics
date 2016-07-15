package com.treelogic.proteus.core.incops.statistics;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

import com.treelogic.proteus.core.configuration.IncrementalConfiguration;
import com.treelogic.proteus.core.configuration.OpParameter;
import com.treelogic.proteus.core.pojos.DataSerie;
import com.treelogic.proteus.core.pojos.IncResult;
import com.treelogic.proteus.core.states.Stateful;
import com.treelogic.proteus.core.utils.FieldUtils;

public abstract class IncrementalOperation<IN, OUT extends Stateful<Double>>
		extends RichWindowFunction<IN, IncResult<OUT>, Tuple, GlobalWindow> {
	
	/**
	 * Seriel version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A common logger
	 */
	protected Log log = LogFactory.getLog(this.getClass());

	/**
	 * Stores the window state. It contains a [key,value] map, where key is the keyed field(s) and value is the result computed
	 * for the current window
	 */
	protected ValueStateDescriptor<Map<String, OUT>> stateDescriptor;

	/**
	 * Shows if the state is already initialized
	 */
	private boolean isStateInitialized;
	
	/**
	 * Contains parameters and configurations given by a user. See @see com.treelogic.proteus.flink.incops.config.IncrementalConfiguration
	 * for more information
	 */
	protected IncrementalConfiguration configuration;

	/**
	 * It returns the number of required data series to compute a window
	 * @return Number of required data series to compute a window
	 */
	protected abstract int numberOfRequiredDataSeries();

	/**
	 * When a window is filled, this method is automatically invoked. 
	 * @param field Keyed field value(s)
	 * @param dataSeries Lists of  values for the keyed fields.
	 * @param status Current state for each parameter
	 */
	protected abstract void updateWindow(String field, List<DataSerie> dataSeries, OUT status);

	/**
	 * Due to reflecion and generyc types API restrictions, we need a naive instance of Stateful<?> to dynamically instantiate new states.
	 */
	private Stateful<?> stateful;

	/**
	 * A constructor with a user-given configuration and a naive instance of Stateful<?>
	 * @param configuration The user-given configuration
	 * @param stateful A naive instance of the Stateful class
	 */
	public IncrementalOperation(IncrementalConfiguration configuration, Stateful<?> stateful) {
		this.configuration = configuration;
		this.stateful = stateful;

		if (configuration.getFields() == null || configuration.getFields().length < 1) {
			throw new IllegalArgumentException("Illegal number of fields");
		}
		initializeDescriptor();
	}
	
	/**
	 * This method is executed every time a window is filled. For more information, you can see {@link RichWindowFunction#apply(Object, org.apache.flink.streaming.api.windowing.windows.Window, Iterable, Collector)}
	 */
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
	private void createState() throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
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