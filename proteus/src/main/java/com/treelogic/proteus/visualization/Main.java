package com.treelogic.proteus.visualization;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.treelogic.proteus.visualization.model.Barchart;
import com.treelogic.proteus.visualization.model.Linechart;
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
		
		//
		//
		// LINECHART
		//
		//
		Linechart<Date, Integer> linechart = new Linechart<Date, Integer>();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
		Date d1 = null, d2 = null, d3 = null;
		try {
			d1 = sdf.parse("01/16");
			d2 = sdf.parse("02/16");
			d3 = sdf.parse("03/16");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		linechart
			.addPoint(new BasicPoint<Date, Integer>(d1, 1))
			.addPoint(new BasicPoint<Date, Integer>(d2, 1))
			.addPoint(new BasicPoint<Date, Integer>(d3, 1));

		System.out.println(barchart.toJson());
		System.out.println(streamgraph.toJson());
		System.out.println(linechart.toJson());
	}
}