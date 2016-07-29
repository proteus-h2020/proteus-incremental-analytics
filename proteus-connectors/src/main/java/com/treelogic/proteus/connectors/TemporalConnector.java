package com.treelogic.proteus.connectors;

import com.treelogic.proteus.resources.model.IncrementalWindowResult;

public class TemporalConnector extends DefaultConnector {

	private IncrementalWindowResult data;
	
	public TemporalConnector(IncrementalWindowResult data){
		this.data = data;		
	}
	
	public void apply(){
		log.debug(data.toString());
	}
}
