package com.treelogic.proteus.connectors;

import com.treelogic.proteus.resources.model.IncResult;

public class TemporalConnector extends DefaultConnector {

	private IncResult data;
	

	public TemporalConnector(IncResult data){
		this.data = data;		
	}
	
	public void apply(){
		log.debug(data.toString());
	}
	
	

}
