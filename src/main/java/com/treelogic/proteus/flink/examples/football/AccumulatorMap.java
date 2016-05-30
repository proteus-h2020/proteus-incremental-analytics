package com.treelogic.proteus.flink.examples.football;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;

public class AccumulatorMap implements MapFunction<Tuple1<Integer>, Tuple2<Integer, Integer>> {
	 
	 
    /**
	 * Auto-generated serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Accumulator map function
	 */
	public Tuple2<Integer, Integer> map(Tuple1<Integer> n) throws Exception {
            return new Tuple2<Integer, Integer>(n.f0, new Integer(1));
    }
}
