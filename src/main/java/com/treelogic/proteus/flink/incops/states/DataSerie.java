package com.treelogic.proteus.flink.incops.states;

import java.util.Arrays;
import java.util.List;

public class DataSerie {

	private String fieldName;
	private List<Double> values;

	public List<Double> values() {
		return values;
	}

	public DataSerie values(List<Double> values) {
		this.values = values;
		return this;
	}

	public DataSerie field(String fieldName) {
		this.fieldName = fieldName;
		return this;
	}

	public static List<DataSerie> asList(DataSerie... series) {
		return Arrays.asList(series);
	}

	@Override
	public String toString() {
		return "DataSerie [fieldName=" + fieldName + ", values=" + values + "]";
	}
	

}
