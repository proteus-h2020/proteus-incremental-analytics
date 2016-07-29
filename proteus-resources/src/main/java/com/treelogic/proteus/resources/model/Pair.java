package com.treelogic.proteus.resources.model;

public class Pair<T0, T1> {

	public T0 key;
	public T1 value;

	public Pair() {
	}

	public Pair(T0 value0, T1 value1) {
		this.key = value0;
		this.value = value1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Pair<?, ?> other = (Pair<?, ?>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pair [f0=" + key + ", f1=" + value + "]";
	}

	public T0 getKey() {
		return key;
	}

	public T1 getValue() {
		return value;
	}

}