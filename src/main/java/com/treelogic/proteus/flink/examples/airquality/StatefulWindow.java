package com.treelogic.proteus.flink.examples.airquality;


import java.util.Date;
import java.util.Iterator;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

public class StatefulWindow extends RichWindowFunction<AirRegister, Tuple3<String, Double, Date>, Tuple, GlobalWindow>{


	private static final long serialVersionUID = 1L;
	//value - num of elements
	ValueStateDescriptor<Tuple2<Double,Integer>> stateDescriptor = 
			new ValueStateDescriptor<Tuple2<Double,Integer>>(
					"last-result",
					new TypeHint<Tuple2<Double,Integer>>() {}.getTypeInfo(),
					null
	);

	public void apply(Tuple key, GlobalWindow window,
			Iterable<AirRegister> iterator, Collector<Tuple3<String,Double, Date>> collector)
			throws Exception {
		//Get last window interaction values
		ValueState<Tuple2<Double, Integer>> state = getRuntimeContext().getState(stateDescriptor);
		Tuple2<Double,Integer> lastRegisters = state.value();
		double lastRecord  = 0.0;
		int lastNumberOfElememets = 0;
		if(lastRegisters != null){
	        lastRecord = lastRegisters.f0;
	        lastNumberOfElememets = lastRegisters.f1;
		}

        
		Iterator<AirRegister> it = iterator.iterator();
		double sum = 0; 
		AirRegister r = null;
		while(it.hasNext()){
			r = it.next();
			sum+=r.getO3();
		}
				
		double currentAVG = sum / (double) Program.WINDOW_SIZE;
				
		double actualAVG = lastRecord != 0.0 
				? (((lastRecord * lastNumberOfElememets)
							+ (currentAVG * Program.WINDOW_SIZE)) 
							/ (Program.WINDOW_SIZE + lastNumberOfElememets))

				: currentAVG;
		
		//Update status 
		double newValue = actualAVG;
		int newNumbeOfElements = lastRegisters != null 
				? lastRegisters.f1 = (lastNumberOfElememets + Program.WINDOW_SIZE)
				: Program.WINDOW_SIZE;
				
		state.update(new Tuple2<Double, Integer>(newValue, newNumbeOfElements));

		collector.collect(new Tuple3<String,Double, Date>(key.toString(), newValue, new Date()));
        
	}
	
}