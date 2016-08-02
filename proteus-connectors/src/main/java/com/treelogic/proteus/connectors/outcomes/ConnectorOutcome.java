package com.treelogic.proteus.connectors.outcomes;

import java.util.ArrayList;
import java.util.List;

import com.treelogic.proteus.resources.model.Pair;

public class ConnectorOutcome {
	private String key;
	private List<Pair<String, Double>> values = new ArrayList<Pair<String, Double>>();

	public ConnectorOutcome(String windowKey) {
		this.key = windowKey;
	}

	public String getKey() {
		return key;
	}

	public List<Pair<String, Double>> getValues() {
		return values;
	}

	public void setValues(List<Pair<String, Double>> pair) {
		this.values = pair;
	}

	@Override
	public String toString() {
		return "ConnectorOutcome [key=" + key + ", pairs=" + values + "]";
	}
	

}
