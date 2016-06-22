package com.treelogic.proteus.flink.incops.util;

import java.util.List;

public class StatefulPearsonCorrelation {
	
	private StatefulVariance xVariance = new StatefulVariance(),
			yVariance = new StatefulVariance();
	private StatefulCovariance covariance = new StatefulCovariance();
	
	public double apply(List<Double> xElems, List<Double> yElems) {
		double x = xVariance.apply(xElems);
		double y = yVariance.apply(yElems);
		double c = covariance.apply(xElems, yElems);
		
		return c / (Math.sqrt(x * y));
	}

}
