package com.treelogic.proteus.flink.incops.util;

import java.io.Serializable;
import java.util.List;

public abstract class Stateful<T> implements Serializable {


	private static final long serialVersionUID = -4422398224135499195L;
	
	protected T value;

	public abstract T value();
	
	public abstract void calculate();
	
	public abstract void add (T value);
	
	public abstract void add (T[] values);
	
	public abstract void add (List<T> values);
	
}
