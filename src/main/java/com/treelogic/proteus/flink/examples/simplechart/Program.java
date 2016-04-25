package com.treelogic.proteus.flink.examples.simplechart;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.treelogic.proteus.flink.examples.utils.WebBrowser;
import com.treelogic.proteus.flink.sink.WebsocketSink;
import com.treelogic.proteus.network.WebsocketServer;
import com.treelogic.proteus.visualization.model.Barchart;
import com.treelogic.proteus.visualization.model.points.BasicPoint;


public class Program {

	public static void main(String[] args) throws Exception {
		String htmlTemplate = "file://" + Program.class.getResource("barchart.html").getPath();
		WebsocketServer.start();
		//wait for websocket initialization
		WebBrowser.start(htmlTemplate);
		
		List<BasicPoint<Integer, Integer>> data = new ArrayList<BasicPoint<Integer, Integer>>();
		for(int i = 0 ; i < 50; i++){
			data.add(new BasicPoint<Integer, Integer>(i, i+1));
		}
		
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		WebsocketSink<Barchart<Integer, Integer>> websocketSink = new WebsocketSink<Barchart<Integer, Integer>>();
		
		DataStream<BasicPoint<Integer, Integer>> stream = env.fromCollection(data);
		
		stream.countWindowAll(1).apply(new ChartWindow()).addSink(websocketSink);
	    env.execute("ChartTest");
		WebsocketServer.stop();
	}

}
