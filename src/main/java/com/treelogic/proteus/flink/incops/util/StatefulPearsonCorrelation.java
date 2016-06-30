package com.treelogic.proteus.flink.incops.util;

import java.util.List;

public class StatefulPearsonCorrelation extends Stateful<Double>{
	
	private StatefulVariance xVariance = new StatefulVariance(),
			yVariance = new StatefulVariance();
	private StatefulCovariance covariance = new StatefulCovariance();
	
	public double apply(List<Double> xElems, List<Double> yElems) {
		double x = xVariance.apply(xElems);
		double y = yVariance.apply(yElems);
		double c = covariance.apply(xElems, yElems);
		
		return c / (Math.sqrt(x * y));
	}

	@Override
	public Double value() {
		calculate();
		return this.value;
	}

	@Override
	public void calculate(List<Double>... values) {
		double x = xVariance.apply(values[0]);
		double y = yVariance.apply(values[1]);
		double c = covariance.apply(values[0], values[1]);
		this.value = c / (Math.sqrt(x * y));
	}

}
