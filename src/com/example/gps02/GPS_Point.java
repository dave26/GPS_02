package com.example.gps02;

public class GPS_Point {
	private long ID;
	private double Latitude;
	private double Longitude;

	public long getID() {
		return ID;
	}

	public void setID(long ID) {
		this.ID = ID;
	}

	public void setLatitude(double Latitude){
		this.Latitude = Latitude;
	}
	public double getLatitude(){
		return Latitude;
	}
	public void setLongitude(double Longitude){
		this.Longitude = Longitude;
	}
	public double getLongitude(){
		return Longitude;
	}
}
