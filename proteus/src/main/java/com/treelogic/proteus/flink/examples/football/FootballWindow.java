package com.treelogic.proteus.flink.examples.football;

import java.util.Iterator;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

public class FootballWindow implements AllWindowFunction <Tuple2<Integer, Integer>, Double, GlobalWindow>{
	 
	/**
	 * Auto-generated serial version uid
	 */
	private static final long serialVersionUID = -3345951346021634163L;

	/**
	 * Window apply function
	 */
	public void apply(GlobalWindow window, Iterable<Tuple2<Integer, Integer>> iterable, Collector<Double> collector) {
		Iterator<Tuple2<Integer, Integer>> itr = iterable.iterator();
		int sum=0, count = 0;
	    while (itr.hasNext()) {
	    	Tuple2<Integer, Integer> element = itr.next();
	        sum+= element.f0;
	        count+=element.f1;
	    }
	    collector.collect(new Double((double)sum/(double)count));
	 }
}