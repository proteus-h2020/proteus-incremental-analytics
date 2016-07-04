package com.treelogic.proteus.flink.incops.util;
import java.util.List;

import com.treelogic.proteus.flink.incops.states.DataSerie;

/**
 * Tuple that contains sum and count of elements. Used to compute mean values
 * associatively.
 *
 */
public class StatefulAverage extends Stateful<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6953929745457825750L;
	
	private double sum, count;

	/**
	 * Sum and count = 0
	 */
	public StatefulAverage() {

	}

	public StatefulAverage(double sum, double count) {
		checkCountParameter(count);

		this.sum = sum;
		this.count = count;
	}

	/**
	 * Increments sum by value and count by 1. Same as inc(value, 1).
	 * 
	 * @param value
	 */
	public void inc(double value) {
		inc(value, 1);
	}

	/**
	 * Used to increment both sum and count by the specified values.
	 * 
	 * @param sum
	 * @param count
	 */
	private void inc(double sum, int count) {
		checkCountParameter(count);
		this.sum += sum;
		this.count += count;
    	this.value =  count == 0 ? 0 : sum / count;

	}

	private void checkCountParameter(double count) {
		if (count < 1) {
			throw new IllegalArgumentException("MeanTuple count cannot be less than one");
		}
	}

	@Override
	public Double value() {
    	this.value =  count == 0 ? 0 : sum / count;
		return this.value;
	}

	@Override
	public void apply(List<DataSerie> series) {
		DataSerie serie = series.get(0);
		for (double number : serie.values()) {
			inc(number);
		}
		this.value = (count == 0 ? 0 : sum / count);
	}

}