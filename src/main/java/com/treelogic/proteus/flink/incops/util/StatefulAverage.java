package com.treelogic.proteus.flink.incops.util;

import java.util.ArrayList;
import java.util.List;

import com.treelogic.proteus.flink.incops.states.DataSerie;
import com.treelogic.proteus.utils.MathUtils;

/**
 * Tuple that contains sum and count of elements. Used to compute mean values
 * associatively.
 *
 */
public class StatefulAverage extends Stateful<Double> {

	private static final long serialVersionUID = 6953929745457825750L;

	private double mean, count;

	private DataSerie serie;

	public StatefulAverage() {
		this.serie = new DataSerie().values(new ArrayList<Double>());
	}

	public StatefulAverage(double mean, double count) {
		checkCountParameter(count);

		this.mean = mean;
		this.count = count;
	}

	private void checkCountParameter(double count) {
		if (count < 1) {
			throw new IllegalArgumentException("MeanTuple count cannot be less than one");
		}
	}

	@Override
	public Double value() {
		this.value = this.mean;
		return this.value;
	}

	@Override
	public void apply(List<DataSerie> series) {
		this.serie = series.get(0);
		List<Double> values = serie.values();
		mean(values);

	}

	private void mean(List<Double> values) {
		int serieSize = values.size();

		double currentAverage = MathUtils.mean(values);
		
		double actualAVG = this.count != 0.0
				? (((this.mean * this.count) + (currentAverage * serieSize)) / (serieSize + this.count))
				: currentAverage;

		this.count += serieSize;
		this.mean = actualAVG;
	}

	public void inc(List<Double> values) {
		this.serie.values().addAll(values);
		mean(this.serie.values());
	}

}