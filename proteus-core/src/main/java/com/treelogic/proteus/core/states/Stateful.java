package com.treelogic.proteus.core.states;

import java.io.Serializable;
import java.util.List;

import com.treelogic.proteus.core.pojos.DataSerie;


public abstract class Stateful<T> implements Serializable {

	protected static final long serialVersionUID = -4422398224135499195L;

	protected T value;

	public abstract T value();

	public abstract void apply(List<DataSerie> values);
}
