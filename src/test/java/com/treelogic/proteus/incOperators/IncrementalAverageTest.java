package com.treelogic.proteus.incOperators;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.flinkspector.core.collection.ExpectedRecords;
import org.flinkspector.datastream.DataStreamTestBase;
import org.flinkspector.datastream.DataStreamTestEnvironment;
import org.junit.BeforeClass;
import org.junit.Test;

import com.treelogic.proteus.flink.examples.airquality.AirRegister;
import com.treelogic.proteus.flink.incops.IncrementalAverage;

public class IncrementalAverageTest extends DataStreamTestBase {

	@BeforeClass
	public static void beforeClass() {
		try {
			DataStreamTestEnvironment env = DataStreamTestEnvironment
					.createTestEnvironment(1);
			env.setTimeoutInterval(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void oneWindowTest() {
		DataStream<Double> stream = this
	            .createTestStream(createDataset(7))
	            .keyBy("station")
	            .countWindow(7)
	            .apply(new IncrementalAverage<AirRegister>("o3"))
	            .map(new Tuple2ToDouble());

        ExpectedRecords<Double> expected =
            new ExpectedRecords<Double>()
            	.expect(30.428571428571427d);

	    assertStream(stream, expected);
	}

	@Test
	public void twoWindowTest() {
		DataStream<Double> stream = this
	            .createTestStream(createDataset(14))
	            .keyBy("station")
	            .countWindow(7)
	            .apply(new IncrementalAverage<AirRegister>("o3"))
	            .map(new Tuple2ToDouble());

        ExpectedRecords<Double> expected =
            new ExpectedRecords<Double>()
            	.expectAll(asList(30.428571428571427d, 26.714285714285715d));

	    assertStream(stream, expected);
	}

	private List<AirRegister> createDataset(int size) {
        String station = "station";
        List<Double> o3registers = asList(
                34d, 8d, 41d, 11d, 9d, 87d, 23d, // First window values
                15d, 1d, 22d, 31d, 90d, 0d, 2d); // Second window values

        List<AirRegister> dataset = new ArrayList<>(size);

        for(int i=0; i < size; i++) {
            AirRegister ar = new AirRegister();
            ar.setStation(station);
            ar.setO3(o3registers.get(i));
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
