package com.treelogic.proteus.core.incops.statistics;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.flinkspector.core.collection.ExpectedRecords;
import org.flinkspector.datastream.DataStreamTestBase;
import org.junit.Test;

import com.treelogic.proteus.core.configuration.IncrementalConfiguration;
import com.treelogic.proteus.core.configuration.OpParameter;
import com.treelogic.proteus.core.pojos.AirRegister;
import com.treelogic.proteus.core.states.StatefulVariance;
import com.treelogic.proteus.core.utils.TestUtils;

public class IncrementalVarianceTest extends DataStreamTestBase {

    @Test
    public void simpleTest() {
		IncrementalConfiguration conf = new IncrementalConfiguration();
		conf.fields(new OpParameter("o3"));
    	
		DataStream<List<Double>> stream = this
            .createTestStream(createDataset())
            .keyBy("station")
            .countWindow(7)
            .apply(new IncrementalVariance<AirRegister>(conf))
            .map(new TestUtils.IncResult2ToDouble<StatefulVariance>());

		  ExpectedRecords<List<Double>> expected = new ExpectedRecords<List<Double>>()
				  .expectAll(asList(
                asList(786.6190476190476d), asList(844.989010989011d)));

        assertStream(stream, expected);
        
    }

    private List<AirRegister> createDataset() {
        String station = "station";
        List<Double> o3registers = asList(
                34d, 8d, 41d, 11d, 9d, 87d, 23d, // First window values
                15d, 1d, 22d, 31d, 90d, 0d, 2d); // Second window values

        List<AirRegister> dataset = new ArrayList<>(14);

        for(Double d : o3registers) {
            AirRegister ar = new AirRegister();
            ar.setStation(station);
            ar.setO3(d);
            dataset.add(ar);
        }

        return dataset;
    }
}