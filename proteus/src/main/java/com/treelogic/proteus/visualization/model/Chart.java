package com.treelogic.proteus.visualization.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.treelogic.proteus.visualization.model.interfaces.Jsonizable;

public abstract class Chart<T> implements Jsonizable {
	/**
	 * Common logger
	 */
	private Log logger = LogFactory.getLog(this.getClass());

	/**
	 * Chart type. It depends on class name.
	 */
	private final String type = this.getClass().getSimpleName();

	/**
	 * List of points
	 */
	protected List<T> points;

	/**
	 * Default constructor
	 */
	public Chart() {
		this.points = new ArrayList<T>();
	}

	/**
	 * This constructor Create a new chart with the specified points
	 * 
	 * @param points
	 *            List of points to be added to the chart
	 */
	public Chart(List<T> points) {
		this.points = points;
	}

	/**
	 * This method converts the scoped current into a JSON string.
	 */
	public String toJson() {
		ObjectWriter ow = new ObjectMapper()
				.setSerializationInclusion(Include.NON_NULL).writer();
		try {
			return ow.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error(e);
			return "{}";
		}
	}

	/**
	 * Returns chart type
	 * 
	 * @return chart type
	 */

	public String getType() {
		return type;
	}

	public List<T> getPoints() {
		return points;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((logger == null) ? 0 : logger.hashCode());
		result = prime * result + ((points == null) ? 0 : points.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chart<?> other = (Chart<?>) obj;
		if (logger == null) {
			if (other.logger != null)
				return false;
		} else if (!logger.equals(other.logger))
			return false;
		if (points == null) {
			if (other.points != null)
				return false;
		} else if (!points.equals(other.points))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}
