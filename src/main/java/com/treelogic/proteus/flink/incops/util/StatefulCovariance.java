package com.treelogic.proteus.flink.incops.util;

import java.util.List;

public class StatefulCovariance {
	
	private double elemsProcessed = 0;
	private double sumDeviationProducts = 0;
	private MeanTuple xMeanTuple = new MeanTuple();
	private MeanTuple yMeanTuple = new MeanTuple();

	public double apply(List<Double> xElems, List<Double> yElems) {
		double xMean = mean(xElems), yMean = mean(yElems);
		double SDP = sumDeviationProducts(xElems, yElems, xMean, yMean);

		if(elemsProcessed == 0) {
            elemsProcessed = xElems.size();
            sumDeviationProducts = SDP;
            updateMeanTuple(xMeanTuple, xElems);
            updateMeanTuple(yMeanTuple, yElems);
        } else {
            double xMeanDiff = xMean - xMeanTuple.mean();
            double yMeanDiff = yMean - yMeanTuple.mean();

            sumDeviationProducts = sumDeviationProducts + SDP
            		+ (elemsProcessed * xElems.size() * xMeanDiff * yMeanDiff
            				/ (elemsProcessed + xElems.size()));
            updateMeanTuple(xMeanTuple, xElems);
            updateMeanTuple(yMeanTuple, yElems);
            elemsProcessed += xElems.size();
        }
		
		return sumDeviationProducts / (elemsProcessed - 1);
	}
	
	private void updateMeanTuple(MeanTuple mt, List<Double> elems) {
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
	
	private double mean(List<Double> elems) {
		double sum = 0;
		
		for(Double d : elems) {
			sum += d;
		}
		
		return sum / elems.size();
	}
}
