package com.treelogic.proteus.core.utils;

import java.util.Arrays;
import java.util.List;

public class MathUtils {

	public static double mean(Double... values) {
		return mean(Arrays.asList(values));

	}

	public static double mean(List<Double> elems) {
		double sum = 0;

		for (Double d : elems) {
			sum += d;
		}

		return sum / elems.size();
	}

}
