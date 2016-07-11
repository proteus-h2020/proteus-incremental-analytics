package com.treelogic.proteus.flink.incops;

import java.util.List;
import com.treelogic.proteus.flink.incops.config.IncrementalConfiguration;
import com.treelogic.proteus.flink.incops.states.DataSerie;
import com.treelogic.proteus.flink.incops.util.StatefulPearsonCorrelation;

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
