package com.treelogic.proteus.incOperators;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.flinkspector.core.collection.ExpectedRecords;
import org.flinkspector.datastream.DataStreamTestBase;
import org.junit.Test;

import com.treelogic.proteus.flink.examples.pojos.AirRegister;

public class IncrementalModeTest extends DataStreamTestBase {
/**
    @Test
    public void test() {
        DataStream<List<Tuple2<Double, Integer>>> stream =
            createTestStream(createDataset())
            .keyBy("station")
            .countWindow(5)
            .apply(new IncrementalMode<AirRegister>("o3"));

        Tuple2<Double, Integer> t1 = new Tuple2<>(4d, 2),
            t2 = new Tuple2<>(7d, 3),
            t3 = new Tuple2<>(4d, 4);

        List<Tuple2<Double, Integer>> l1 = new ArrayList<>(1),
            l2 = new ArrayList<>(1),
            l3 = new ArrayList<>(1);

        l1.add(t1);
        l2.add(t2);
        l3.add(t3);

        ExpectedRecords<List<Tuple2<Double, Integer>>> expected =
            new ExpectedRecords<List<Tuple2<Double, Integer>>>().expectAll(
                asList(l1, l2, l3));
        
        assertStream(stream, expected);
    }

    private List<AirRegister> createDataset() {
        List<Double> nums = Arrays.asList(new Double[]{
            5d, 7d, 4d, 4d, 9d,
            7d, 7d, 5d, 8d, 9d,
            8d, 8d, 1d, 4d, 4d});

        List<AirRegister> dataset = new ArrayList<>(15);

        for(Double d : nums) {
            AirRegister ar = new AirRegister();
            ar.setO3(d);
            dataset.add(ar);
        }

        return dataset;
    }
    **/
}