package com.treelogic.proteus.resources.model;

public class AirRegister {

	private String date;
	private double o3, so2, no, no2, pm10, co;
	private double latitude, longitude;
	private String station;

	public AirRegister() {}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getO3() {
		return o3;
	}

	public void setO3(double o3) {
		this.o3 = o3;
	}

	public double getSo2() {
		return so2;
	}

	public void setSo2(double so2) {
		this.so2 = so2;
	}

	public double getNo() {
		return no;
	}

	public void setNo(double no) {
		this.no = no;
	}

	public double getNo2() {
		return no2;
	}

	public void setNo2(double no2) {
		this.no2 = no2;
	}

	public double getPm10() {
		return pm10;
	}

	public void setPm10(double pm10) {
		this.pm10 = pm10;
	}

	public double getCo() {
		return co;
	}

	public void setCo(double co) {
		this.co = co;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(co);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(no);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(no2);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(o3);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(pm10);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(so2);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((station == null) ? 0 : station.hashCode());
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
		AirRegister other = (AirRegister) obj;
		if (Double.doubleToLongBits(co) != Double.doubleToLongBits(other.co))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		if (Double.doubleToLongBits(no) != Double.doubleToLongBits(other.no))
			return false;
		if (Double.doubleToLongBits(no2) != Double.doubleToLongBits(other.no2))
			return false;
		if (Double.doubleToLongBits(o3) != Double.doubleToLongBits(other.o3))
			return false;
		if (Double.doubleToLongBits(pm10) != Double
				.doubleToLongBits(other.pm10))
			return false;
		if (Double.doubleToLongBits(so2) != Double.doubleToLongBits(other.so2))
			return false;
		if (station == null) {
			if (other.station != null)
				return false;
		} else if (!station.equals(other.station))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AirRegister [date=" + date + ", o3=" + o3 + ", so2=" + so2
				+ ", no=" + no + ", no2=" + no2 + ", pm10=" + pm10 + ", co="
				+ co + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", station=" + station + "]";
	}

}
