package com.treelogic.proteus.connectors.outcomes;

public interface ConnectorOutputStrategy{

	/**
	 * 
	 * Transform an object into a specific format
	 * @param t Object
	 * @return A string containing the object
	 */
	public String output(Object t);
}
