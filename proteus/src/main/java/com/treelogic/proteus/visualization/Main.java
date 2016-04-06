package com.treelogic.proteus.visualization;

import java.util.Date;

import com.treelogic.proteus.visualization.model.Barchart;
import com.treelogic.proteus.visualization.model.Streamgraph;
import com.treelogic.proteus.visualization.model.points.BasicPoint;
import com.treelogic.proteus.visualization.model.points.StreamPoint;

public class Main {
	public static void main (String [] args){
				//
		//
		// BARCHART 
		//
		//
		Barchart<String, Integer> barchart = new Barchart<String, Integer>();
		barchart
			.addPoint(new BasicPoint<String, Integer>("España", 2))
			.addPoint(new BasicPoint<String, Integer>("Hungría", 5))
			.addPoint(new BasicPoint<String, Integer>("Alemania", 1));
		//
		//
		// STREAMGRAPH 
		//
		//
		Streamgraph<Date, Integer, String> streamgraph = new Streamgraph<Date, Integer, String>();
		streamgraph
			.addPoint(new StreamPoint<Date, Integer, String>(new Date(),1,2, "A"))
			.addPoint(new StreamPoint<Date, Integer, String>(new Date(),3,4, "B"))
			.addPoint(new StreamPoint<Date, Integer, String>(new Date(),5,6, "C"));
				
		
		System.out.println(barchart.toJson());
		System.out.println(streamgraph.toJson());
	}
}
