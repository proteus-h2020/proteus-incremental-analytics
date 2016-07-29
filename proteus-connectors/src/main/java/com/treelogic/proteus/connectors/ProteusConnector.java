package com.treelogic.proteus.connectors;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treelogic.proteus.resources.model.IncrementalWindowResult;

public abstract class ProteusConnector implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3629386847283854464L;
	
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	public abstract ProteusConnector apply(IncrementalWindowResult<?> windowData);
	public abstract String toJson();


}
