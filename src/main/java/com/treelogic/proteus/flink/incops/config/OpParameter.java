package com.treelogic.proteus.flink.incops.config;

import java.io.Serializable;

public class OpParameter implements Serializable{


	private static final long serialVersionUID = -586927409949942409L;

	private String[] fields;

	public OpParameter(String... fields) {
		this.fields = fields;
	}

	public boolean isSingleField() {
		return !isNull() && this.fields.length == 1;
	}

	private boolean isNull() {
		return this.fields == null;
	}
	
	public String getComposedName(){
		if(isNull()){
			return "";
		}
		if(this.fields.length == 1){
			return this.fields[0];
		}
		StringBuilder builder = new StringBuilder();
		for(String field : this.fields){
			builder.append(field);
			builder.append("_");
		}
		return builder.toString();
	}

	public String[] getFields() {
		return fields;
	}
	
}
