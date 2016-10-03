package com.treelogic.proteus.resources.states;

import com.treelogic.proteus.resources.model.DataSerie;
import com.treelogic.proteus.resources.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Tuple that contains sum and count of elements. Used to compute mean values
 * associatively.
 *
 */
public class StatefulMode extends Stateful {

	private static final long serialVersionUID = 6953929745457825750L;

	private double mode, count;

	private DataSerie serie;

	public StatefulMode() {
		this.serie = new DataSerie().values(new ArrayList<Double>());
	}

	public StatefulMode(double mode, double count) {
		checkCountParameter(count);

		this.mode = mode;
		this.count = count;
	}

	private void checkCountParameter(double count) {
		if (count < 1) {
			throw new IllegalArgumentException("ModeTuple count cannot be less than one");
		}
	}

	@Override
	public Double value() {
		this.value = this.mode;
		return this.value;
	}

	@Override
	public void apply(List<DataSerie> series) {
		this.serie = series.get(0);
		List<Double> values = serie.values();
		mode(values);

	}

	private void mode(List<Double> values) {

	}

	public void inc(List<Double> values) {
		this.serie.values().addAll(values);
		mode(this.serie.values());
	}

}