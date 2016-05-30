package com.treelogic.proteus.visualization.model;

import java.util.List;
import com.treelogic.proteus.visualization.model.interfaces.ChartAction;
import com.treelogic.proteus.visualization.model.points.StreamPoint;

public class Streamgraph<X, Y, K> extends Chart<StreamPoint<X, Y, K>> implements
		ChartAction<StreamPoint<X, Y, K>> {

	/**
	 * Default constructor
	 */
	public Streamgraph() {
		super();
	}

	/**
	 * This constructor Create a new chart with the specified points
	 * 
	 * @param points
	 *            List of points to be added to the chart
	 */
	public Streamgraph(List<StreamPoint<X, Y, K>> points) {
		super(points);
	}

	/**
	 * Add a new point to the current chart
	 * 
	 * @param point
	 *            New point to be added
	 * @return chart chart instance with the new point added
	 */
	public Streamgraph<X, Y, K> addPoint(StreamPoint<X, Y, K> point) {
		this.points.add(point);
		return this;
	}

	/**
	 * Add a new collection of points to the current chart
	 * 
	 * @param point
	 *            New point to be added
	 * @return chart chart instance with the new point added
	 */
	public Streamgraph<X, Y, K> addPoints(List<StreamPoint<X, Y, K>> points) {
		this.points.addAll(points);
		return this;
	}
}
