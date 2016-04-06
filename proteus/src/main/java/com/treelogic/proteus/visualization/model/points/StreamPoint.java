package com.treelogic.proteus.visualization.model.points;

public class StreamPoint<X,Y,K> extends Point {
	
	/**
	 * 'X' chart coordinate. Its type could be a string or numeric
	 */
	private X x;
	
	/**
	 * 'y0' chart coordinate. Its type could be a string or numeric
	 */
	private Y y0;
	
	/**
	 * 'y1' chart coordinate. Its type could be a string or numeric
	 */
	private Y y1;
	
	/**
	 * A string key for identifying an object
	 */
	private K key;
	
	/**
	 * Default constructor
	 */
	public StreamPoint(){
		
	}
	
	/**
	 * This constructor receives all the needed parameters for the chart creation
	 * @param x 'X' coordinate
	 * @param y0 'y0' coordinate
	 * @param y1 'y1' coordinate
	 * @param key String identifier
	 */
	public StreamPoint(X x, Y y0, Y y1, K key){
		this.x = x;
		this.y0 = y0;
		this.y1 = y1;
		this.key = key;
	}
	
	public X getX() {
		return x;
	}
	public void setX(X x) {
		this.x = x;
	}
	public Y getY0() {
		return y0;
	}
	public void setY0(Y y0) {
		this.y0 = y0;
	}
	public Y getY1() {
		return y1;
	}
	public void setY1(Y y1) {
		this.y1 = y1;
	}
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y0 == null) ? 0 : y0.hashCode());
		result = prime * result + ((y1 == null) ? 0 : y1.hashCode());
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
		StreamPoint<X,Y,K> other = (StreamPoint<X,Y,K>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y0 == null) {
			if (other.y0 != null)
				return false;
		} else if (!y0.equals(other.y0))
			return false;
		if (y1 == null) {
			if (other.y1 != null)
				return false;
		} else if (!y1.equals(other.y1))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "StreamPoint [x=" + x + ", y0=" + y0 + ", y1=" + y1 + ", key=" + key + "]";
	}
}
