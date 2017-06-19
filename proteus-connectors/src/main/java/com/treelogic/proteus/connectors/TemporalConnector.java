package com.treelogic.proteus.connectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import com.treelogic.proteus.connectors.outcomes.ConnectorData;
import com.treelogic.proteus.connectors.outcomes.ConnectorJsonStrategy;
import com.treelogic.proteus.connectors.outcomes.ConnectorOutcome;
import com.treelogic.proteus.connectors.outcomes.ConnectorOutputStrategy;
import com.treelogic.proteus.resources.model.IncrementalWindowResult;
import com.treelogic.proteus.resources.states.Stateful;
import com.treelogic.proteus.resources.utils.FieldUtils;

public class TemporalConnector extends ProteusConnector {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fieldName;

	public TemporalConnector(String fieldName, ConnectorOutputStrategy outputStrategy) {
		this.fieldName = fieldName;
		this.outputStrategy = outputStrategy;
	}

	public TemporalConnector(String fieldName) {
		this(fieldName, new ConnectorJsonStrategy());
	}

	public TemporalConnector(String fieldName, String dataFormat) {
		this.fieldName = fieldName;
	}

	@Override
	public TemporalConnector apply(IncrementalWindowResult<?> windowData) {
		Object lastRecordWindow = windowData.getLastWindowRecord();
		String windowKey = windowData.getWindowKey();
		@SuppressWarnings("unused")
		String lastRecordTemporalField = "";
		try {
			lastRecordTemporalField = FieldUtils.getValue(lastRecordWindow, this.fieldName);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		//ConnectorOutcome result = new ConnectorOutcome(windowKey);
		ConnectorOutcome result = new ConnectorOutcome();

		List<ConnectorData> data = new ArrayList<ConnectorData>();
		result.setData(data);
		
		for(Entry<String, Stateful> entry : windowData.values().entrySet()){
			ConnectorData d = new ConnectorData(entry.getKey());
			List<Point> points = new ArrayList<Point>();
			points.add(new Point(lastRecordTemporalField, entry.getValue().value()));
			d.setValues(points);
			data.add(d);
		}
		
		/**
		List<Point> pairs = new ArrayList<Point>();
		for (Entry<String, Stateful> entry : windowData.values().entrySet()) {
			Point point = new Point();
			point.setX(entry.getKey());
			point.setY(entry.getValue().value());
			pairs.add(point);
		}
		result.setValues(pairs);
		this.connectorOutcome = result;
		**/
		this.connectorOutcome = result;
		return this;
	}

	public String output() {
		return this.outputStrategy.output(connectorOutcome);
	}
}
