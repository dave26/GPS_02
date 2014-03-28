package com.example.gps02;

public class Entry {
	private long ID;
	private double Latitude;
	private double Longitude;

	public long getID() {
		return ID;
	}

	public void setID(long ID) {
		this.ID = ID;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude (double Latitude) {
		this.Latitude = Latitude;
	}
	
	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double Longitude) {
		this.Longitude = Longitude;
	}

}