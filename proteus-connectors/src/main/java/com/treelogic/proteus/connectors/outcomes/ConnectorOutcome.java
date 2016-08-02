package com.treelogic.proteus.connectors.outcomes;

import java.util.ArrayList;
import java.util.List;
public class ConnectorOutcome {

	
   // var data = [{ key: "series1", values: series1 }, { key: "series2", values: series2 }, { key: "series3", values: series3 }];

	
    private List<ConnectorData> data = new ArrayList<ConnectorData>();

	public List<ConnectorData> getData() {
		return data;
	}

	public void setData(List<ConnectorData> data) {
		this.data = data;
	}
     
    
    
	/**
	private String key;
	private String type = "Linechart";
	private String chart = "chart1";
	private List<ConnectorData> data = new ArrayList<ConnectorData>();



	public ConnectorOutcome(String windowKey) {
		this.key = windowKey;
	}

	public String getKey() {
		return key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChart() {
		return chart;
	}

	public void setChart(String chart) {
		this.chart = chart;
	}

	public List<ConnectorData> getData() {
		return data;
	}

	public void setData(List<ConnectorData> data) {
		this.data = data;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chart == null) ? 0 : chart.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConnectorOutcome other = (ConnectorOutcome) obj;
		if (chart == null) {
			if (other.chart != null)
				return false;
		} else if (!chart.equals(other.chart))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConnectorOutcome [key=" + key + ", type=" + type + ", chart=" + chart + ", data=" + data + "]";
	}
**/
}

