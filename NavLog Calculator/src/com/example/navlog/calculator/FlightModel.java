package com.example.navlog.calculator;


import java.util.Stack;

import android.widget.ImageView;

public class FlightModel 
{
	private Stack<Waypoint> points = new Stack<Waypoint>();
	private Waypoint currentLocation;
	private Waypoint departureLocation;
	private Waypoint destinationLocation;
	
	public void addWaypoint(ImageView m, double lat, double lon, double alt)
	{
		points.push(new Waypoint(m, lat, lon, alt));
	}
	public Waypoint removeLastWaypoint()
	{
		Waypoint removed =  points.pop();
		return removed;	
	}
	public Waypoint getLastWaypoint()
	{
		Waypoint peek = points.peek();
		return peek;
	}
	public int getWaypointCount()
	{
		return points.size();
	}
	public Stack<Waypoint> getPoints() 
	{
		return points;
	}
	public Waypoint getCurrentLocation() 
	{
		return currentLocation;
	}
	public Waypoint getDepartureLocation() 
	{
		return departureLocation;
	}
	public Waypoint getArrivalLocation() 
	{
		return destinationLocation;
	}
	public void setPoints(Stack<Waypoint> points) 
	{
		this.points = points;
	}
	public void setCurrentLocation(Waypoint currentLocation) 
	{
		this.currentLocation = currentLocation;
	}
	public void setCurrentLocation(ImageView m, double lat, double lon, double alt)
	{
		this.currentLocation = new Waypoint(m, lat, lon,alt);
	}
	
	public void setDepartureLocation(Waypoint departureLocation) 
	{
		this.departureLocation = departureLocation;
	}
	public void setDepartureLocation(ImageView m, double lat, double lon)
	{
		this.departureLocation = new Waypoint(m, lat, lon,0);
	}
	
	public void setArrivalLocation(Waypoint arrivalLocation) 
	{
		this.destinationLocation = arrivalLocation;
	}
	public void setArrivalLocation(ImageView m, double lat, double lon)
	{
		this.destinationLocation = new Waypoint(m, lat, lon,0);
	}
	public double[] getAllWaypointLatitudes()
	{
		int length = this.points.size();
		double[] latitudes = new double[length];
		
		for(int i=0; i<length;i++)
		{
			latitudes[i] = this.points.get(i).getLatitude();
		}
		return latitudes;
	}
	
	public double[] getAllWaypointLongitudes()
	{
		int length = this.points.size();
		double[] longitudes = new double[length];
		
		for(int i=0;i<length;i++)
		{
			longitudes[i] = this.points.get(i).getLongitude();
		}
		return longitudes;
	}
	
	public class Waypoint
	{
		private double latitude;
		private double longitude;
		private double altitude;
		private ImageView marker;
		

		public Waypoint(ImageView m, double lat, double lon, double alt)
		{
			this.setMarker(m);
			this.setLatitude(lat);
			this.setLongitude(lon);
			this.setAltitude(alt);
		}
		
		
		public double getLatitude() {
			return latitude;
		}
		public double getLongitude() {
			return longitude;
		}
		public ImageView getMarker() {
			return marker;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public void setMarker(ImageView marker) {
			this.marker = marker;
		}


		public double getAltitude() {
			return altitude;
		}


		public void setAltitude(double altitude) {
			this.altitude = altitude;
		}
	}


	


}
