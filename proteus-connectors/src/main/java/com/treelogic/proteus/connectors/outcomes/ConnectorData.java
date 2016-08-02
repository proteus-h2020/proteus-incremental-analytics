package com.treelogic.proteus.connectors.outcomes;

import java.util.ArrayList;
import java.util.List;

import com.treelogic.proteus.connectors.Point;

public class ConnectorData {

	private String key;
	private List<Point> values = new ArrayList<Point>();

	public ConnectorData(String key) {
		this.key = key;
	}

	public List<Point> getValues() {
		return values;
	}

	public void setValues(List<Point> values) {
		this.values = values;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}