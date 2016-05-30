package com.treelogic.proteus.flink.examples.football;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class GoalReduce implements ReduceFunction<Tuple2<Integer, Integer>>{
	 
    /**
	 * Auto-generated serial version UID
	 */
	private static final long serialVersionUID = -6735603842623149624L;

	/**
	 * Reduce function
	 */
	public Tuple2<Integer, Integer> reduce(Tuple2<Integer, Integer> prev, Tuple2<Integer, Integer> ele) throws Exception {
		return new Tuple2<Integer, Integer>(prev.f0.intValue() + ele.f0.intValue(),prev.f1.intValue()+1 );
    }
}
