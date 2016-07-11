package com.treelogic.proteus.flink.incops.util;

import java.util.List;

import com.treelogic.proteus.flink.incops.states.DataSerie;

public class StatefulCovariance extends Stateful<Double> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double elemsProcessed = 0;
	private double sumDeviationProducts = 0;
	private StatefulAverage xMeanTuple = new StatefulAverage();
	private StatefulAverage yMeanTuple = new StatefulAverage();

	public void apply(List<DataSerie> values) {
		List<Double> xElems = values.get(0).values();
		List<Double> yElems = values.get(1).values();

		double xMean = mean(xElems), yMean = mean(yElems);
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
		
		this.value =  sumDeviationProducts / (elemsProcessed - 1);
	}
	
	private void updateMeanTuple(StatefulAverage mt, List<Double> elems) {
		mt.inc(elems);
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
	
	private double mean(List<Double> elems) {
		double sum = 0;
		
		for(Double d : elems) {
			sum += d;
		}
		
		return sum / elems.size();
	}

	@Override
	public Double value() {
		return this.value;
	}


}
