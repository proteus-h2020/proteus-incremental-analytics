package com.treelogic.proteus.core.incops.statistics;

import java.util.List;
import com.treelogic.proteus.core.configuration.IncrementalConfiguration;
import com.treelogic.proteus.core.pojos.DataSerie;
import com.treelogic.proteus.core.states.StatefulPearsonCorrelation;

/**
 * Incremental implementation of the Pearson product-moment correlation coefficient
 *
 * @param <IN>
 */
public class IncrementalPearsonCorrelation<IN>
		extends IncrementalOperation<IN, StatefulPearsonCorrelation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IncrementalPearsonCorrelation(IncrementalConfiguration configuration){
		super(configuration, new StatefulPearsonCorrelation());
	}

	@Override
	protected int numberOfRequiredDataSeries() {
		return 2;
	}

	@Override
	protected void updateWindow(String field, List<DataSerie> dataSeries, StatefulPearsonCorrelation status) {
		status.apply(dataSeries);
	}


}
