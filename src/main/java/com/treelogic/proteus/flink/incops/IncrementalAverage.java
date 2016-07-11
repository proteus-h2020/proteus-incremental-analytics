package com.treelogic.proteus.flink.incops;

import java.util.List;
import com.treelogic.proteus.flink.incops.config.IncrementalConfiguration;
import com.treelogic.proteus.flink.incops.states.DataSerie;
import com.treelogic.proteus.flink.incops.util.StatefulAverage;

public class IncrementalAverage<IN> extends IncrementalOperation<IN, StatefulAverage> {

	private static final long serialVersionUID = 1L;

	public IncrementalAverage(IncrementalConfiguration configuration) {
		super(configuration, new StatefulAverage());
	}

	@Override
	protected void updateWindow(String field, List<DataSerie> dataSeries, StatefulAverage status) {
		status.apply(dataSeries);
	}

	@Override
	protected int numberOfRequiredDataSeries() {
		return 1;
	}

}