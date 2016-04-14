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
}
