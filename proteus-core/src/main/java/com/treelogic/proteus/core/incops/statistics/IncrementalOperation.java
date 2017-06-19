package com.treelogic.proteus.core.incops.statistics;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treelogic.proteus.core.configuration.IncrementalConfiguration;
import com.treelogic.proteus.core.configuration.OpParameter;
import com.treelogic.proteus.resources.model.DataSerie;
import com.treelogic.proteus.resources.model.IncrementalWindowResult;
import com.treelogic.proteus.resources.model.IncrementalWindowValue;
import com.treelogic.proteus.resources.states.Stateful;
import com.treelogic.proteus.resources.utils.FieldUtils;

public abstract class IncrementalOperation<IN>
		extends RichWindowFunction<IN, IncrementalWindowResult<?>, Tuple, GlobalWindow> {

	/**
	 * Seriel version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A common logger
	 */
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Stores the window state. It contains a [key,value] map, where key is the
	 * keyed field(s) and value is the result computed for the current window
	 */
	protected ValueStateDescriptor<Map<String, Stateful>> stateDescriptor;

	/**
	 * Contains parameters and configurations given by a user. See @see
	 * com.treelogic.proteus.flink.incops.config.IncrementalConfiguration for
	 * more information
	 */
	protected IncrementalConfiguration configuration;

	/**
	 * It returns the number of required data series to compute a window
	 * 
	 * @return Number of required data series to compute a window
	 */
	protected abstract int numberOfRequiredDataSeries();

	/**
	 * When a window is filled, this method is automatically invoked.
	 * 
	 * @param field
	 *            Keyed field value(s)
	 * @param dataSeries
	 *            Lists of values for the keyed fields.
	 * @param status
	 *            Current state for each parameter
	 */
	protected abstract void updateWindow(String field, List<DataSerie> dataSeries, Stateful status);

	/**
	 * Due to reflecion and generyc types API restrictions, we need a naive
	 * instance of Stateful<?> to dynamically instantiate new states.
	 */
	private Stateful stateful;

	/**
	 * A constructor with a user-given configuration and a naive instance of
	 * Stateful<?>
	 * 
	 * @param configuration
	 *            The user-given configuration
	 * @param stateful
	 *            A naive instance of the Stateful class
	 */
	public IncrementalOperation(IncrementalConfiguration configuration, Stateful stateful) {
		this.configuration = configuration;
		this.stateful = stateful;

		if (configuration.getFields() == null || configuration.getFields().length < 1) {
			throw new IllegalArgumentException("Illegal number of fields");
		}
		initializeDescriptor();
	}

	/**
	 * This method is executed every time a window is filled. For more
	 * information, you can see
	 * {@link RichWindowFunction#apply(Object, org.apache.flink.streaming.api.windowing.windows.Window, Iterable, Collector)}
	 */
	@Override
	public void apply(Tuple key, GlobalWindow window, Iterable<IN> input, Collector<IncrementalWindowResult<?>> out)
			throws Exception {

		OpParameter[] parameters = this.configuration.getFields();
		ValueState<Map<String, Stateful>> state = getRuntimeContext().getState(stateDescriptor);

		Map<String, Stateful> stateValues = state.value();

		if (stateValues.isEmpty()) {
			initializeKeyedState(stateValues, state);
		}

		IncrementalWindowValue values = new IncrementalWindowValue(key.toString());

		// Loop values & fields. Save the last register.
		IN lastRecord = null;
		for (IN in : input) {
			for (OpParameter parameter : parameters) {
				for (String field : parameter.getFields()) {
					Double value = FieldUtils.getValue(in, field);
					values.put(field, value);
				}
			}
			lastRecord = in;
		}
		applyOperation(values, stateValues);
		state.update(stateValues);

		IncrementalWindowResult<IN> result = new IncrementalWindowResult<IN>(key.toString(), lastRecord);

		for (Entry<String, Stateful> entry : stateValues.entrySet()) {
			result.put(entry.getKey(), entry.getValue());
		}
		
		Thread.sleep(1000);

		out.collect(result);
	}

	/**
	 * Apply operation, given a list of keyed values (KEY - VALUE) and a set of
	 * states to match with.
	 * 
	 * @param values
	 *            List of window values
	 * @param states
	 *            List of key states
	 */
	protected void applyOperation(IncrementalWindowValue values, Map<String, Stateful> states) {
		OpParameter[] parameters = this.configuration.getFields();
		List<DataSerie> dataSeries = new ArrayList<DataSerie>();

		for (OpParameter parameter : parameters) {
			dataSeries.clear();
			for (String field : parameter.getFields()) {
				DataSerie serie = new DataSerie(field).values((List<Double>) values.get(field));
				dataSeries.add(serie);
			}
			Stateful state = states.get(parameter.getComposedName());

			if (assertWindowRestriction(dataSeries.size())) {
				updateWindow(parameter.getComposedName(), dataSeries, state);
			}
		}
	}

	/**
	 * Check if operation can be applied given a number of data series.
	 * 
	 * @param series
	 *            Number of data series
	 * @return True if operation can be applied
	 */
	private boolean assertWindowRestriction(int dataSeries) {
		if (dataSeries != numberOfRequiredDataSeries()) {
			throw new IllegalStateException(this.getClass().getSimpleName() + " should receive just "
					+ numberOfRequiredDataSeries() + " data Serie. Instead: " + dataSeries);
		}
		return true;
	}

	/**
	 * Initialize the state descriptor. Automatically invoked in the constructor
	 * of this class.
	 */
	private void initializeDescriptor() {
		stateDescriptor = new ValueStateDescriptor<>("last-result", new TypeHint<Map<String, Stateful>>() {
		}.getTypeInfo(), new HashMap<String, Stateful>());
	}

	/**
	 * Initialize a new state for the current key. Given the keys specified by
	 * user, it creates a new state for each of them.
	 * 
	 * @param stateValues
	 *            Current state values
	 * @param state
	 *            State of this window
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@SuppressWarnings("unchecked")
	private void initializeKeyedState(Map<String, Stateful> stateValues, ValueState<Map<String, Stateful>> state)
			throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		OpParameter[] opParameters = this.configuration.getFields();

		Class<Stateful> clazz = (Class<Stateful>) stateful.getClass();
		for (OpParameter field : opParameters) {
			try {
				stateValues.put(field.getComposedName(), clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		try {
			state.update(stateValues);
			log.debug("State has been correctly initialized: " + stateValues);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}