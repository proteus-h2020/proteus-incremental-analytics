package com.treelogic.proteus.flink.incops;

import java.util.Map;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;
import com.treelogic.proteus.flink.incops.config.IncrementalConfiguration;
import com.treelogic.proteus.flink.incops.entities.IncResult;
import com.treelogic.proteus.flink.incops.util.Stateful;
import com.treelogic.proteus.flink.incops.util.StatefulAverage;

public class IncrementalEmpty<IN> extends IncrementalOperation<IN, StatefulAverage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -682775657498291431L;

	public IncrementalEmpty(IncrementalConfiguration configuration) {
		super(configuration);
	}

	@Override
	public void apply(Tuple key, GlobalWindow window, Iterable<IN> input, Collector<IncResult<StatefulAverage>> out)
			throws Exception {

		//1) Loop data
		
	}

	@Override
	protected void initializeDescriptor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateState(Map<String, Stateful<Double>> status) {
		// TODO Auto-generated method stub
		
	}

	
}
