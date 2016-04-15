package com.treelogic.proteus.visualization.model;

import java.util.List;
import com.treelogic.proteus.visualization.model.interfaces.ChartAction;
import com.treelogic.proteus.visualization.model.points.BasicPoint;

public class Barchart<T, K> extends Chart<BasicPoint<T, K>> implements
		ChartAction<BasicPoint<T, K>> {

	/**
	 * Default constructor
	 */
	public Barchart() {
		super();
	}

	/**
	 * This constructor Create a new chart with the specified points
	 * 
	 * @param points
	 *            List of points to be added to the chart
	 */
	public Barchart(List<BasicPoint<T, K>> points) {
		super(points);
	}

	/**
	 * Add a new point to the current chart
	 * 
	 * @param point
	 *            New point to be added
	 * @return chart chart instance with the new point added
	 */
	public Barchart<T, K> addPoint(BasicPoint<T, K> p) {
		this.points.add(p);
		return this;
	}

	/**
	 * Add a new collection of points to the current chart
	 * 
	 * @param point
	 *            New point to be added
	 * @return chart chart instance with the new point added
	 */
	public Barchart<T, K> addPoints(List<BasicPoint<T, K>> points) {
		this.points.addAll(points);
		return this;
	}
	
	/**
	 * Print object information
	 */
	@Override
	public String toString() {
		return super.toString();
	}
}
