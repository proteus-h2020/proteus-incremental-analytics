package com.treelogic.proteus.incOperators;

import com.treelogic.proteus.flink.examples.airquality.AirRegister;
import com.treelogic.proteus.flink.incops.IncrementalVariance;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.flinkspector.core.collection.ExpectedRecords;
import org.flinkspector.datastream.DataStreamTestBase;
import org.flinkspector.datastream.DataStreamTestEnvironment;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class IncrementalVarianceTest extends DataStreamTestBase {

    private static DataStreamTestEnvironment env = null;

    @BeforeClass
    public static void beforeClass() {
        try {
            env = DataStreamTestEnvironment.createTestEnvironment(1);
            env.setTimeoutInterval(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void simpleTest() {
        DataStream<Double> stream = this
            .createTestStream(createDataset())
            .keyBy("station")
            .countWindow(7)
            .apply(new IncrementalVariance<AirRegister>("o3"));

        ExpectedRecords<Double> expected =
            new ExpectedRecords<Double>().expectAll(asList(
                786.6190476190476d, 844.989010989011d));

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