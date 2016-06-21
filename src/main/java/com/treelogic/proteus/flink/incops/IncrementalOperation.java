package com.treelogic.proteus.flink.incops;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

public abstract class IncrementalOperation<IN, OUT>
		extends RichWindowFunction<IN, OUT, Tuple, GlobalWindow> {
	
	private static final long serialVersionUID = 1L;
	private static final String ERROR = "Field cannot neither be null nor empty";

	protected void checkField(String field) {
		if (field == null || field.isEmpty()) {
			throw new IllegalArgumentException(ERROR);
		}
	}
	
	protected void checkFields(String... fields) {
		for (String field : fields) {
			if (field == null || field.isEmpty()) {
				throw new IllegalArgumentException(ERROR);
			}
		}
	}
}
