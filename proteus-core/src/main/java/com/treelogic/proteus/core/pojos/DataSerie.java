package com.treelogic.proteus.core.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataSerie implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fieldName;
	private List<Double> values = new ArrayList<Double>();

	public List<Double> values() {
		return values;
	}

	public DataSerie (String fieldName){
		this.fieldName = fieldName;
		
	}
	
	public DataSerie(){
		
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
