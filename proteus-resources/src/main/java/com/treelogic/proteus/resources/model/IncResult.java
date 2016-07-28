package com.treelogic.proteus.resources.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.treelogic.proteus.resources.states.Stateful;

public class IncResult{

	/**
	 * String = keyBy (nameOfField)
	 * Tuple2-String = keyBy (value)
	 * Tuple2-T = result

	 */
	private Map<String, Pair<String, Stateful>> results = new HashMap<String, Pair<String, Stateful>>();
	
	public  Set<String> keys(){
		return results.keySet();
	}
	
	public void put(String entryKey, String key, Stateful value){
		results.put(entryKey, new Pair<String, Stateful>(key, value));
	}

	@Override
	public String toString() {
		StringBuilder message = new StringBuilder();
		for(Entry<String, Pair<String, Stateful>> e : results.entrySet()){
			message.append(e.getKey())
					.append("   ->   ")
					.append(e.getValue().f0)
					.append(" - ")
					.append(e.getValue().f1.value())
					.append("\n");
		}
		return message.toString();
	}
	
	public Set<Entry<String, Pair<String, Stateful>>> entrySet(){
		return results.entrySet();
	}
	
	
}
