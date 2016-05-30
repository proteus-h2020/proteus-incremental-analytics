package com.treelogic.proteus.flink.examples.airquality;

import java.util.Date;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.treelogic.proteus.flink.incops.IncrementalAverage;

public class Program {
	
	public static final int WINDOW_SIZE=100;
	
	public static void main(String[] args) throws Exception {
		// set up the execution environment
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		final StreamExecutionEnvironment streamingEnv = StreamExecutionEnvironment.getExecutionEnvironment();

		DataSource<AirRegister> registers = env
				.readCsvFile(Program.class.getResource("aire.csv").getPath())
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
			.apply(new IncrementalAverage<AirRegister>("o3"))
			.writeAsCsv("results_"+ new Date().getTime());
			
		streamingEnv.execute("AirRegisters");
	}

}
