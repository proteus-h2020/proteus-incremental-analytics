package com.treelogic.proteus.visualization.model.points;

public class BasicPoint<T, K> extends Point {
	/**
	 * 'X' chart coordinate. Its type could be a string or numeric
	 */
	private T x;

	/**
	 * 'Y' chart coordinate. its type could be a string or numeric
	 */
	private K y;

	/**
	 * Default constructor
	 */
	public BasicPoint() {

	}

	/**
	 * Constructor that receives two parameters: 'x' and 'y' coordinates
	 * 
	 * @param key
	 *            'x' coordinate
	 * @param value
	 *            'y' coordinate
	 */
	public BasicPoint(T x, K y) {
		this.x = x;
		this.y = y;
	}

	public T getX() {
		return x;
	}

	public K getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
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
		@SuppressWarnings("unchecked")
		BasicPoint<T, K> other = (BasicPoint<T, K>) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pair [key=" + x + ", value=" + y + "]";
	}

}
