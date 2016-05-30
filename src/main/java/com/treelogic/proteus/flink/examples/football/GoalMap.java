package com.treelogic.proteus.flink.examples.football;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple1;

public class GoalMap implements MapFunction<String, Tuple1<Integer>> {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = 1296508221575214114L;

	
	/**
	 * Goal map function
	 */
	public Tuple1<Integer> map(String s) throws Exception {
		String[] fields = s.split("::");

		int local,visit;
		try {
			local = Integer.parseInt(fields[5]);
			visit = Integer.parseInt(fields[6]);
		} catch (NumberFormatException e) {
			return new Tuple1<Integer>(new Integer(0));
		}
		return new Tuple1<Integer>(new Integer(local + visit));
	}
}