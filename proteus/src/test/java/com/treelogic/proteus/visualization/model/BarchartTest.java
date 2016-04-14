package com.treelogic.proteus.visualization.model;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.treelogic.proteus.visualization.model.points.BasicPoint;

public class BarchartTest {
	static ObjectMapper mapper = new ObjectMapper();

	@Test
	public void newBarchart() throws JsonParseException, JsonMappingException, IOException {
		Chart<BasicPoint<Integer, String>> expected = new Barchart<Integer, String>();
		String jsonString = expected.toJson();
		@SuppressWarnings("unchecked")
		Barchart<Integer, String> actual = mapper.readValue(jsonString, Barchart.class);		
		assertEquals(actual, expected);
	}
}
