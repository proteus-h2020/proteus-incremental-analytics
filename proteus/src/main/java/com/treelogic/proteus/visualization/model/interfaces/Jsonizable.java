package com.treelogic.proteus.visualization.model.interfaces;

public interface Jsonizable {
	/**
	 * Converts an object into a JSON string
	 * 
	 * @return JSON string that contains all the object properties
	 */
	public String toJson();
}
