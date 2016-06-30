package com.treelogic.proteus.flink.incops.util;

import java.util.List;

import com.treelogic.proteus.utils.MathUtils;

public class StatefulCovariance extends Stateful<Double> {
	
	/**
	 * Number of element processed
	 */
	private double elemsProcessed = 0;
	
	/**
	 * The sum deviation of products
	 */
	private double sumDeviationProducts = 0;
	
	/**
	 * List of X values
	 */
	private StatefulAverage xMeanTuple = new StatefulAverage();
	
	/**
	 * List of Y values
	 */
	private StatefulAverage yMeanTuple = new StatefulAverage();

	@Override
	public Double value() {
		return this.value;
	}


	

	public double apply(List<Double> xElems, List<Double> yElems) {
		double xMean = MathUtils.mean(xElems), yMean = MathUtils.mean(yElems);
		double SDP = sumDeviationProducts(xElems, yElems, xMean, yMean);

		// TODO Use strategy pattern
		if(elemsProcessed == 0) {
            elemsProcessed = xElems.size();
            sumDeviationProducts = SDP;
            updateMeanTuple(xMeanTuple, xElems);
            updateMeanTuple(yMeanTuple, yElems);
        } else {
            double xMeanDiff = xMean - xMeanTuple.value();
            double yMeanDiff = yMean - yMeanTuple.value();

            sumDeviationProducts = sumDeviationProducts + SDP
            		+ (elemsProcessed * xElems.size() * xMeanDiff * yMeanDiff
            				/ (elemsProcessed + xElems.size()));
            updateMeanTuple(xMeanTuple, xElems);
            updateMeanTuple(yMeanTuple, yElems);
            elemsProcessed += xElems.size();
        }
		
		this.value = sumDeviationProducts / (elemsProcessed - 1);
		return this.value;
	}
	
	private void updateMeanTuple(StatefulAverage mt, List<Double> elems) {
		for(Double d : elems) {
			mt.inc(d);
		}
	}
	
	private double sumDeviationProducts(List<Double> xElems,
			List<Double> yElems, double xMean, double yMean) {
		double SDP = 0;
		
		for(int i = 0; i < xElems.size(); i++) {
			double x = xElems.get(i), y = yElems.get(i);
			SDP = SDP + ((x - xMean) * (y - yMean));
		}
		
		return SDP;
	}

	@Override
	public void calculate(List<Double>... values) {
		// TODO Auto-generated method stub
		
	}
}
