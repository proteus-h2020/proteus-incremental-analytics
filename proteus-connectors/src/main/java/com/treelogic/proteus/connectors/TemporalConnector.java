package com.treelogic.proteus.connectors;

import com.treelogic.proteus.resources.model.IncrementalWindowValue;

public class TemporalConnector extends DefaultConnector {

	private IncrementalWindowValue data;
	
	public TemporalConnector(IncrementalWindowValue data){
		this.data = data;		
	}
	
	public void apply(){
		log.debug(data.toString());
	}
}
