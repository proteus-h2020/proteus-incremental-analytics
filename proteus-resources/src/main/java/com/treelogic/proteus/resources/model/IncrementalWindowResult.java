package com.treelogic.proteus.resources.model;

import java.util.HashMap;
import java.util.Map;

import com.treelogic.proteus.resources.states.Stateful;

public class IncrementalWindowResult {

	private Map<String, Stateful> values = new HashMap<String, Stateful>();

	private String windowKey;

	public IncrementalWindowResult(String windowKey) {
		this.windowKey = windowKey;
	}

	public void put(String key, Stateful state){
		values.put(key,  state);
	}
	
	public Map<String, Stateful> values(){
		return this.values;
	}
	
	public String getWindowKey(){
		return windowKey;
	}
	
}
