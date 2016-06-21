package com.treelogic.proteus.incOperators;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.flinkspector.core.collection.ExpectedRecords;
import org.flinkspector.datastream.DataStreamTestBase;
import org.junit.Test;

import com.treelogic.proteus.flink.examples.airquality.AirRegister;
import com.treelogic.proteus.flink.incops.IncrementalCorrelation;

public class IncrementalCorrelationTest extends DataStreamTestBase {
	
	@Test
	public void windowCorrelationTest() {
		DataStream<Double> stream =
	            createTestStream(createDataset(7))
	            .keyBy("station")
	            .countWindow(7)
	            .apply(new IncrementalCorrelation<AirRegister>("o3", "co"))
	            .map(new Tuple2ToDouble());

	        ExpectedRecords<Double> expected =
	            new ExpectedRecords<Double>().expect(-0.360746551183269d);

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
	
	private static class Tuple2ToDouble
	implements MapFunction<Tuple2<String, Double>, Double> {

	private static final long serialVersionUID = 1L;

	@Override
	public Double map(Tuple2<String, Double> arg0) throws Exception {
		return arg0.f1;
	}

}
}
