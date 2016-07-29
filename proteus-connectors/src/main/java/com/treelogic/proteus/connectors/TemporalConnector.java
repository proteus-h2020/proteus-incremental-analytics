package com.treelogic.proteus.connectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.treelogic.proteus.resources.model.IncrementalWindowResult;
import com.treelogic.proteus.resources.model.Pair;
import com.treelogic.proteus.resources.states.Stateful;
import com.treelogic.proteus.resources.utils.FieldUtils;

public class TemporalConnector extends ProteusConnector {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fieldName;
	
	private ConnectorOutcome connectorOutcome;
	
	public TemporalConnector(String fieldName){
		this.fieldName = fieldName;
	}

	public TemporalConnector(String fieldName, String dataFormat){
		this.fieldName = fieldName;
	}
	
	@Override
	public TemporalConnector apply(IncrementalWindowResult<?> windowData) {
		Object lastRecordWindow = windowData.getLastWindowRecord();
		String windowKey = windowData.getWindowKey();
		String lastRecordTemporalField = "";
		try {
			lastRecordTemporalField = FieldUtils.getValue(lastRecordWindow, this.fieldName);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		ConnectorOutcome result = new ConnectorOutcome(windowKey);
		List<Pair<String, Double>> pairs = new ArrayList<Pair<String, Double>>();
		for(Entry<String, Stateful> entry : windowData.values().entrySet()){
			Pair<String, Double> pair = new Pair<String, Double>();
			pair.key = entry.getKey();
			pair.value = entry.getValue().value();
			pairs.add(pair);
		}
		result.setPairs(pairs);
		this.connectorOutcome = result;
		return this;
	}

	@Override
	public String toJson() {
		if(this.connectorOutcome != null){
			org.json.JSONObject jsonObject = new org.json.JSONObject(this.connectorOutcome);
			return jsonObject.toString();
		}
		else{
			throw new IllegalStateException("Cannot convert to JSON the result of this connector. The connector outcome is null");
		}
	}
}
