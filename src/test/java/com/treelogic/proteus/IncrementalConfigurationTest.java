package com.treelogic.proteus;

import org.junit.Test;

import com.treelogic.proteus.flink.incops.config.IncrementalConfiguration;
import com.treelogic.proteus.flink.incops.config.OpParameter;

import static org.junit.Assert.*;


public class IncrementalConfigurationTest {


	@Test
	public void testFields() {
		IncrementalConfiguration configuration = new IncrementalConfiguration();
		configuration.fields(new OpParameter("o3"), new OpParameter("so2"));
		assertEquals(2, configuration.getFields().length);
	}
}
