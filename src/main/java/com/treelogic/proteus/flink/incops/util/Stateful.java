package com.treelogic.proteus.flink.incops.util;

import java.io.Serializable;
import java.util.List;

import com.treelogic.proteus.flink.incops.states.DataSerie;

public abstract class Stateful<T> implements Serializable {


	private static final long serialVersionUID = -4422398224135499195L;
	
	protected T value;

	public abstract T value();
		
	public abstract void apply (List<DataSerie> values);
	
}
