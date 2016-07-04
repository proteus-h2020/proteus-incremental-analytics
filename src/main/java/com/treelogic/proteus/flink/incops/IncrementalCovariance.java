package com.treelogic.proteus.flink.incops;

import java.util.List;
import com.treelogic.proteus.flink.incops.config.IncrementalConfiguration;
import com.treelogic.proteus.flink.incops.states.DataSerie;
import com.treelogic.proteus.flink.incops.util.StatefulCovariance;

/**
 * Incremental covariance using the updating formula described in
 * http://prod.sandia.gov/techlib/access-control.cgi/2008/086212.pdf
 *
 * @param <IN>
 *            Pojo type that contains the field to be analysed
 */
public class IncrementalCovariance<IN> extends IncrementalOperation<IN, StatefulCovariance> {

	private static final long serialVersionUID = 1L;

	public IncrementalCovariance(IncrementalConfiguration configuration) {
		super(configuration, new StatefulCovariance());
	}

	@Override
	protected int numberOfRequiredDataSeries() {
		return 2;
	}

	@Override
	protected void updateWindow(String field, List<DataSerie> dataSeries, StatefulCovariance status) {
		status.apply(dataSeries);
	}


}
