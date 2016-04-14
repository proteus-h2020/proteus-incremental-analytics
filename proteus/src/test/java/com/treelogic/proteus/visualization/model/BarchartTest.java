package com.treelogic.proteus.visualization.model;

import org.junit.Test;

import com.treelogic.proteus.visualization.model.points.BasicPoint;

public class BarchartTest {

	@Test
	public void newBarchart() {
		Chart<BasicPoint<Integer, String>> barchart = new Barchart<Integer, String>();
		String actual = barchart.toJson();
		String expected = "{\"type\": \"Barchart\" \"series\": []}";
		// assertEquals(result, expected);
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
