package com.treelogic.proteus.flink.examples.football;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.treelogic.proteus.flink.sink.WebsocketSink;

public class Program {

	
	public static void main (String [] args) throws Exception{

		// set up the execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// get input data (works with both data-at-rest and data-in-motion)
		String filepath = Program.class.getResource("dataset.txt").getPath();
		DataStream<String> lines = env.readTextFile(filepath);

		KeyedStream<Tuple2<Integer, Integer>, Tuple> keyed = lines.map(new GoalMap())
				.map(new AccumulatorMap()) // transform element -> <element, 1>
				.keyBy(1);

		keyed.reduce(new GoalReduce()) // cumulative add and count <sum, n_ele>
			.countWindowAll(10) // each N elements
            .apply(new FootballWindow()) // calculate the weighted average of averages
            .addSink(new WebsocketSink<Double>());

      // execute program
      env.execute("IncrementalAVG");
	}
}
