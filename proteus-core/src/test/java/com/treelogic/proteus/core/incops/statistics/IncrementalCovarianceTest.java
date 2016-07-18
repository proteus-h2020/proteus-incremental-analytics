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
import com.treelogic.proteus.core.states.StatefulCovariance;
import com.treelogic.proteus.core.utils.TestUtils;

public class IncrementalCovarianceTest extends DataStreamTestBase {


	
    @Test
    public void naiveCovarianceTest() {
		IncrementalConfiguration conf = new IncrementalConfiguration();
		conf.fields(new OpParameter("o3","co"));
		
        DataStream<List<Double>> stream =
            createTestStream(createDataset(7))
            .keyBy("station")
            .countWindow(7)
            .apply(new IncrementalCovariance<AirRegister>(conf))
            .map(new TestUtils.IncResult2ToDouble<StatefulCovariance, Double>());


        ExpectedRecords<List<Double>> expected =
            new ExpectedRecords<List<Double>>().expect(asList(-340.3333333333333d));

        assertStream(stream, expected);
    }

    @Test
    public void combinedCovarianceTest() {
		IncrementalConfiguration conf = new IncrementalConfiguration();
		conf.fields(new OpParameter("o3","co"));
		
        DataStream<List<Double>> stream =
            createTestStream(createDataset(14))
            .keyBy("station")
            .countWindow(7)
            .apply(new IncrementalCovariance<AirRegister>(conf))
            .map(new TestUtils.IncResult2ToDouble<StatefulCovariance, Double>());

        ExpectedRecords<List<Double>> expected =
            new ExpectedRecords<List<Double>>().expectAll(asList(
                asList(-340.3333333333333d),
                asList(-322.4945054945055d)));

        assertStream(stream, expected);
    }

    private List<AirRegister> createDataset(int datasetSize) {
    	 String station = "station";

         List<Double> o3registers = asList(
             4d, 8d, 41d, 11d, 9d, 87d, 23d,
             43d, 12d, 9d, 98d, 23d, 28d, 65d);

         List<Double> coRegisters = asList(
             15d, 1d, 22d, 31d, 90d, 0d, 2d,
             87d, 26d, 61d, 1d, 9d, 33d, 8d);

         // Return dataset
         List<AirRegister> dataset = new ArrayList<>(datasetSize);

         for(int i = 0; i < datasetSize; i++) {
             Double o3 = o3registers.get(i);
             Double co = coRegisters.get(i);
             AirRegister ar = new AirRegister();
             ar.setStation(station);
             ar.setO3(o3);
             ar.setCo(co);
             dataset.add(ar);
         }
         return dataset;
    }
    
    
}