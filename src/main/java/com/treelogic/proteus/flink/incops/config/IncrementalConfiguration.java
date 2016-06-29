package com.treelogic.proteus.flink.incops.config;

import java.io.Serializable;

public class IncrementalConfiguration implements Serializable{


	private static final long serialVersionUID = 3660071657881168289L;
	private String[] fields;
	private Class<?> to;

	public IncrementalConfiguration() {

	}

	public IncrementalConfiguration fields(String... fields) {
		this.fields = fields;
		return this;
	}
	
	public IncrementalConfiguration to(Class<?> to){
		this.to = to;
		return this;
	}

	public String[] getFields() {
		return fields;
	}

	public Class<?> getTo() {
		return to;
	}

}
