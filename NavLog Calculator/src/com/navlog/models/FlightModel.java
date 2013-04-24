package com.navlog.models;


import java.util.Stack;

import android.widget.ImageView;

public class FlightModel 
{
	private Stack<Waypoint> points = new Stack<Waypoint>();
	private Waypoint currentLocation;
	private Waypoint departureLocation;
	private Waypoint destinationLocation;
	private String departureICAO;
	private String destinationICAO;
	
	/**
	 * Constructor Modelo Informacion de Vuelo.
	 * @param depICAO codigo ICAO del aeropuerto de Salida
	 * @param destICAO codigo ICAO del aeropuerto de Llegada
	 */
	public FlightModel(String depICAO, String destICAO)
	{
		this.setDepartureICAO(depICAO);
		this.setDestinationICAO(destICAO);			
	}
	
	public FlightModel(double[] lat, double[]lon, String depICAO, double depLat, double depLon, String destICAO, double destLat, double destLon)
	{
		//departureLocation = new Waypoint()
	}
	public void addWaypoint(ImageView m, double lat, double lon, double alt)
	{
		points.push(new Waypoint(m, lat, lon, alt));
	}
	
	public void addWaypoint(Waypoint waypoint)
	{
		points.push(waypoint);
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
	
	public double[] getAllWaypointAltitudes()
	{
		int lenght = this.points.size();
		double[] altitudes = new double[lenght];
		for(int i=0;i<lenght;i++)
		{
			altitudes[i] = this.points.get(i).getAltitude();
		}
		return altitudes;
	}
	
	public String getDepartureICAO() {
		return departureICAO;
	}
	public void setDepartureICAO(String departureICAO) {
		this.departureICAO = departureICAO;
	}

	public String getDestinationICAO() {
		return destinationICAO;
	}
	public void setDestinationICAO(String destinationICAO) {
		this.destinationICAO = destinationICAO;
	}

	public class Waypoint
	{
		private double latitude;
		private double longitude;
		private double altitude;
		private ImageView marker;
		
		public Waypoint()
		{
			
		}
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
