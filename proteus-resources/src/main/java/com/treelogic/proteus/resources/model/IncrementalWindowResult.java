package com.treelogic.proteus.resources.model;

import java.util.HashMap;
import java.util.Map;

import com.treelogic.proteus.resources.states.Stateful;

public class IncrementalWindowResult<T> {

	private Map<String, Stateful> values = new HashMap<String, Stateful>();

	private String windowKey;
	private T lastWindowRecord;

	public IncrementalWindowResult(String windowKey, T lastWindowRecord) {
		this.windowKey = windowKey;
		this.lastWindowRecord = lastWindowRecord;
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
	public T getLastWindowRecord(){
		return lastWindowRecord;
	}
	
}
