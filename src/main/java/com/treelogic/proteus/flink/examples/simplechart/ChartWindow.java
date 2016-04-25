package com.treelogic.proteus.flink.examples.simplechart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import com.treelogic.proteus.visualization.model.Barchart;
import com.treelogic.proteus.visualization.model.points.BasicPoint;

public class ChartWindow implements AllWindowFunction<BasicPoint<Integer, Integer>, Barchart<Integer, Integer>, GlobalWindow>{

	/**
	 * Auto-generated serial version uid
	 */
	private static final long serialVersionUID = -6227081665867426163L;

	/**
	 * Apply method description
	 */
	public void apply(GlobalWindow arg0,
			Iterable<BasicPoint<Integer, Integer>> iterator,
			Collector<Barchart<Integer, Integer>> collector) throws Exception {
		List<BasicPoint<Integer, Integer>> points = new ArrayList<BasicPoint<Integer, Integer>>();
		Iterator<BasicPoint<Integer, Integer>> it = iterator.iterator();
		while(it.hasNext()){
			points.add(it.next());
		}
		Barchart<Integer, Integer> barchart = new Barchart<Integer, Integer>(points);
		collector.collect(barchart);
		//Simulate heavy operation
		Thread.sleep(200);
		
	}
	
}