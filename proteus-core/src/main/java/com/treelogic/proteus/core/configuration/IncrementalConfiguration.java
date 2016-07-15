package com.treelogic.proteus.core.configuration;

import java.io.Serializable;

public class IncrementalConfiguration implements Serializable{


	private static final long serialVersionUID = 3660071657881168289L;
	private OpParameter[] fields;
	private Class<?> to;

	public IncrementalConfiguration() {

	}

	public IncrementalConfiguration fields(OpParameter... fields) {
		this.fields = fields;
		return this;
	}
	
	public IncrementalConfiguration to(Class<?> to){
		this.to = to;
		return this;
	}

	public OpParameter[] getFields() {
		return fields;
	}

	public Class<?> getTo() {
		return to;
	}

}
