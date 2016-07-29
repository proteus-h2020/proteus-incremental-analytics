package com.treelogic.proteus.connectors;

import java.util.ArrayList;
import java.util.List;

import com.treelogic.proteus.resources.model.Pair;

public class ConnectorOutcome {

	private String key;
	private List<Pair<String, Double>> pairs = new ArrayList<Pair<String, Double>>();

	public ConnectorOutcome(String windowKey) {
		this.key = windowKey;
	}

	public String getKey() {
		return key;
	}

	public List<Pair<String, Double>> getPairs() {
		return pairs;
	}

	public void setPairs(List<Pair<String, Double>> pair) {
		this.pairs = pair;
	}

	@Override
	public String toString() {
		return "ConnectorOutcome [key=" + key + ", pairs=" + pairs + "]";
	}
	

}
