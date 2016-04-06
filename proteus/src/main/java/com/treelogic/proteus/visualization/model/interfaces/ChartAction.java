package com.treelogic.proteus.visualization.model.interfaces;

import java.util.List;

import com.treelogic.proteus.visualization.model.Chart;
import com.treelogic.proteus.visualization.model.points.Point;

public interface ChartAction <T extends Point> {
	/**
	 * Add a new point to chart
	 * @param p Point to be added
	 */
	public Chart<T> addPoint(T p);
	
	/**
	 * Add a list of points to chart
	 * @param points List of points to be added
	 */
	public Chart<T> addPoints(List<T> points);
}
