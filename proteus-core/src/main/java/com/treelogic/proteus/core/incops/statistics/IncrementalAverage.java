package com.treelogic.proteus.core.incops.statistics;

import java.util.List;
import com.treelogic.proteus.core.configuration.IncrementalConfiguration;
import com.treelogic.proteus.core.pojos.DataSerie;
import com.treelogic.proteus.core.states.StatefulAverage;

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