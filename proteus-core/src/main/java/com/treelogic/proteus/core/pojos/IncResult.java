package com.treelogic.proteus.core.pojos;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.flink.api.java.tuple.Tuple2;

import com.treelogic.proteus.core.states.Stateful;


public class IncResult <T extends Stateful<?>>{
	/**
	 * String = keyBy (nameOfField)
	 * Tuple2-String = keyBy (value)
	 * Tuple2-T = result

	 */
	private Map<String, Tuple2<String, T>> results = new HashMap<String, Tuple2<String, T>>();
	
	public  Set<String> keys(){
		return results.keySet();
	}
	
	public void put(String entryKey, String key, T value){
		results.put(entryKey, new Tuple2<String, T>(key, value));
	}

	@Override
	public String toString() {
		StringBuilder message = new StringBuilder();
		for(Entry<String, Tuple2<String, T>> e : results.entrySet()){
			message.append(e.getKey())
					.append("   ->   ")
					.append(e.getValue().f0)
					.append(" - ")
					.append(e.getValue().f1.value())
					.append("\n");
		}
		return message.toString();
	}
	
	public Set<Entry<String, Tuple2<String, T>>> entrySet(){
		return results.entrySet();
	}
	
	
}
