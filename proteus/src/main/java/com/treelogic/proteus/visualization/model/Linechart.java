package com.treelogic.proteus.visualization.model;

import com.treelogic.proteus.visualization.model.interfaces.ChartAction;
import com.treelogic.proteus.visualization.model.points.BasicPoint;

import java.util.List;

public class Linechart<T,K> extends Chart<BasicPoint<T,K>> implements ChartAction<BasicPoint<T,K>>{

	/**
	 * Default constructor
	 */
	public Linechart(){
		super();
	}

	/**
	 * This constructor Create a new linechart with the specified points
	 * @param points List of points to be added to the chart
	 */
	public Linechart(List<BasicPoint<T, K>> points){
		super(points);
	}
		
	/**
	 * Add a new point to the current chart
	 * @param p New point to be added
	 * @return chart chart instance with the new point added
	 */
	public Linechart<T,K> addPoint(BasicPoint<T,K> p) {
		this.points.add(p);
		return this;
	}
	
	/**
	 * Add a new collection of points to the current linechart
	 * @param points New points to be added
	 * @return chart chart instance with the new point added
	 */
	public Linechart<T,K> addPoints(List<BasicPoint<T, K>> points) {
		this.points.addAll(points);
		return this;
	}
	
}
