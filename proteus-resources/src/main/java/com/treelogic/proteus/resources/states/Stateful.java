package com.treelogic.proteus.resources.states;

import java.io.Serializable;
import java.util.List;

import com.treelogic.proteus.resources.model.DataSerie;

public abstract class Stateful implements Serializable {

	protected static final long serialVersionUID = -4422398224135499195L;

	protected Double value;

	public abstract Double value();

	public abstract void apply(List<DataSerie> values);
}
