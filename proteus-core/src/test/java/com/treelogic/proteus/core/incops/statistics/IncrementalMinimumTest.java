package com.treelogic.proteus.core.incops.statistics;

import com.treelogic.proteus.core.configuration.IncrementalConfiguration;
import com.treelogic.proteus.core.configuration.OpParameter;
import com.treelogic.proteus.core.utils.TestUtils;
import com.treelogic.proteus.resources.model.AirRegister;
import com.treelogic.proteus.resources.states.StatefulAverage;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.flinkspector.core.collection.ExpectedRecords;
import org.flinkspector.datastream.DataStreamTestBase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;


public class IncrementalMinimumTest extends DataStreamTestBase {

	@Test
	public void oneWindowTest() {
		IncrementalConfiguration conf = new IncrementalConfiguration();
		conf.fields(new OpParameter("o3"));
		DataStream<List<Double>> stream = this
	            .createTestStream(createDataset(7))
	            .keyBy("station")
	            .countWindow(7)
	            .apply(new IncrementalMinimum<AirRegister>(conf))
	            .map(new TestUtils.IncResult2ToDouble<StatefulAverage>());
		
        ExpectedRecords<List<Double>> expected = new ExpectedRecords<List<Double>>()
            	.expect(asList(8d));

	    assertStream(stream, expected);
	}

	@Test
	public void twoWindowTest() {
		IncrementalConfiguration conf = new IncrementalConfiguration();
		conf.fields(new OpParameter("o3"));
		
		DataStream<List<Double>> stream = this
	            .createTestStream(createDataset(14))
	            .keyBy("station")
	            .countWindow(7)
	            .apply(new IncrementalMinimum<AirRegister>(conf))
	            .map(new TestUtils.IncResult2ToDouble<StatefulAverage>());
		

        ExpectedRecords<List<Double>> expected =
                new ExpectedRecords<List<Double>>()
                	.expectAll(asList(asList(8d), asList(0d)));

    	    assertStream(stream, expected);
	}

	private List<AirRegister> createDataset(int size) {
        String station = "station";
        List<Double> o3registers = asList(
                34d, 8d, 41d, 11d, 90d, 87d, 23d, // First window values
                15d, 1d, 22d, 31d, 9d, 0d, 2d); // Second window values

        List<AirRegister> dataset = new ArrayList<>(size);

        for(int i=0; i < size; i++) {
            AirRegister ar = new AirRegister();
            ar.setStation(station);
            ar.setO3(o3registers.get(i));
            dataset.add(ar);
        }

        return dataset;
    }
	


}