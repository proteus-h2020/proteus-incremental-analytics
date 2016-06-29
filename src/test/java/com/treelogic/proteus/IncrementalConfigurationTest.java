package com.treelogic.proteus;

import org.junit.Test;

import com.treelogic.proteus.flink.incops.config.IncrementalConfiguration;
import com.treelogic.proteus.visualization.model.Barchart;

import static org.junit.Assert.*;


public class IncrementalConfigurationTest {


	@Test
	public void testFields() {
		IncrementalConfiguration configuration = new IncrementalConfiguration();
		configuration.fields("o3", "so2");
		assertEquals(2, configuration.getFields().length);
	}
	
	@Test
	public void testChart() {
		IncrementalConfiguration configuration = new IncrementalConfiguration();
		configuration.fields("o3", "so2").to(Barchart.class);
		assertEquals(Barchart.class, configuration.getTo());
	}
}
