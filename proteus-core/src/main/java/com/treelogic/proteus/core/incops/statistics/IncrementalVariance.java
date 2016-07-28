package com.treelogic.proteus.core.incops.statistics;

import java.util.List;

import com.treelogic.proteus.core.configuration.IncrementalConfiguration;
import com.treelogic.proteus.core.pojos.DataSerie;
import com.treelogic.proteus.core.states.Stateful;
import com.treelogic.proteus.core.states.StatefulVariance;

/**
 * Incremental variance using the general updating formula described in
 * http://i.stanford.edu/pub/cstr/reports/cs/tr/79/773/CS-TR-79-773.pdf
 *
 * @param <IN>
 * Pojo type that contains the field to be analysed
 */

public class IncrementalVariance<IN> extends IncrementalOperation<IN> {

	private static final long serialVersionUID = 1L;

	public IncrementalVariance(IncrementalConfiguration configuration) {
		super(configuration, new StatefulVariance());
	}

	@Override
	protected void updateWindow(String field, List<DataSerie> dataSeries, Stateful status) {
		status.apply(dataSeries);
	}

	@Override
	protected int numberOfRequiredDataSeries() {
		return 1;
	}

}
