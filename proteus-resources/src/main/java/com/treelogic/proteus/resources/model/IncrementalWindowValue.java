package com.treelogic.proteus.resources.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class IncrementalWindowValue {

	private Map<String, List<Double>> values = new HashMap<String, List<Double>>();

	private String windowKey;

	public IncrementalWindowValue(String windowKey) {
		this.windowKey = windowKey;
	}

	public void put(String key, Double value) {
		List<Double> keyedValues = values.get(key);
		if (keyedValues == null) {
			keyedValues = new ArrayList<Double>();
			values.put(key, keyedValues);
		}
		keyedValues.add(value);
	}

	public List<Double> get(String key) {
		return values.get(key);
	}

	public Map<String, List<Double>> values() {
		return values;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Entry<String, List<Double>> entry : values.entrySet()) {
			builder.append(entry.getKey());
			builder.append(":");
			builder.append(entry.getValue());
			builder.append("\n");
		}
		return builder.toString();
	}

	public String getWindowKey() {
		return windowKey;
	}
}
