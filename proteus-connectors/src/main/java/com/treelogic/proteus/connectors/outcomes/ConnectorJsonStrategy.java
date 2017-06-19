package com.treelogic.proteus.connectors.outcomes;

import java.io.Serializable;

public class ConnectorJsonStrategy implements ConnectorOutputStrategy, Serializable {

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String output(Object t) {
		if (t != null) {
			org.json.JSONObject jsonObject = new org.json.JSONObject(t);
			;
			return jsonObject.get("data").toString();
		} else {
			throw new IllegalStateException(
					"Cannot convert to JSON the result of this connector. The connector outcome is null");
		}
	}

}
