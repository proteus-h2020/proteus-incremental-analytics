package com.treelogic.proteus.visualization.model;

import org.junit.Test;

import com.treelogic.proteus.visualization.model.points.BasicPoint;

public class StreamgraphTest {

	@Test
	public void newStreamgraph() {
	
	}

	@Test
	public void addSomePoints() {
		Barchart<String, Integer> barchart = new Barchart<String, Integer>();
		barchart.addPoint(new BasicPoint<String, Integer>("España", 2))
				.addPoint(new BasicPoint<String, Integer>("Hungría", 5))
				.addPoint(new BasicPoint<String, Integer>("Alemania", 1));

		String actual = barchart.toJson();
		String expected = "";
		// assertEquals(expected, actual);
	}
}
