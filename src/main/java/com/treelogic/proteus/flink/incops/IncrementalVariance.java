package com.treelogic.proteus.flink.incops;

import java.util.List;

import com.treelogic.proteus.flink.incops.config.IncrementalConfiguration;
import com.treelogic.proteus.flink.incops.pojos.DataSerie;
import com.treelogic.proteus.flink.incops.states.StatefulVariance;

/**
 * Incremental variance using the general updating formula described in
 * http://i.stanford.edu/pub/cstr/reports/cs/tr/79/773/CS-TR-79-773.pdf
 *
 * @param <IN>
 * Pojo type that contains the field to be analysed
 */

public class IncrementalVariance<IN> extends IncrementalOperation<IN, StatefulVariance> {

	private static final long serialVersionUID = 1L;

	public IncrementalVariance(IncrementalConfiguration configuration) {
		super(configuration, new StatefulVariance());
	}

	@Override
	protected void updateWindow(String field, List<DataSerie> dataSeries, StatefulVariance status) {
		status.apply(dataSeries);
	}

	@Override
	protected int numberOfRequiredDataSeries() {
		return 1;
	}

}
