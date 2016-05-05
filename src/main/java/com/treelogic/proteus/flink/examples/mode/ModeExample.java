package com.treelogic.proteus.flink.examples.mode;

import com.treelogic.proteus.flink.examples.airquality.AirRegister;
import com.treelogic.proteus.flink.incops.IncrementalMode;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class ModeExample {

    public static final int WINDOW_SIZE = 3;

    public static void main(String[] args) throws Exception {
        // set up the execution environment
        final ExecutionEnvironment env = ExecutionEnvironment
          .getExecutionEnvironment();
        final StreamExecutionEnvironment streamingEnv = StreamExecutionEnvironment.getExecutionEnvironment();

        DataSource<AirRegister> registers = env
          .readCsvFile(ModeExample.class.getResource
            ("smallDataset.csv").getPath())
          .ignoreFirstLine()
          .pojoType(
            AirRegister.class,
            "date",
            "o3",
            "so2",
            "no",
            "no2",
            "pm10",
            "co",
            "latitude",
            "longitude",
            "station"
          );

        DataStream<AirRegister> stream = streamingEnv.fromCollection(registers.collect());

        stream.keyBy("station")
          .countWindow(WINDOW_SIZE)
          .apply(new IncrementalMode<AirRegister>("o3"))
          .print();

        streamingEnv.execute("AirRegisters");
    }

}
