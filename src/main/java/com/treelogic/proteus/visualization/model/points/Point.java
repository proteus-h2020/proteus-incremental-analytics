package com.treelogic.proteus.visualization.model.points;

public abstract class Point {

	/**
	 * This object contains information about a given point
	 */
	private PointDetails details;

	public PointDetails getDetails() {
		return details;
	}

	public void setDetails(PointDetails details) {
		this.details = details;
	}

}
