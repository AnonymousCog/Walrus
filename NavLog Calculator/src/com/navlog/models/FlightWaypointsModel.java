package com.navlog.models;


import java.util.Stack;

import android.widget.ImageView;

public class FlightWaypointsModel implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 620210547609466539L;
	protected Stack<Waypoint> points = new Stack<Waypoint>();
	protected Waypoint currentLocation;
	protected Waypoint departure;
	protected Waypoint destination;
	protected String departureICAO;
	protected String destinationICAO;
	
	/**
	 * Constructor Modelo Informacion de Vuelo.
	 * @param depICAO codigo ICAO del aeropuerto de Salida
	 * @param destICAO codigo ICAO del aeropuerto de Llegada
	 */
	public FlightWaypointsModel(String depICAO, String destICAO)
	{
		this.setDepartureICAO(depICAO);
		this.setDestinationICAO(destICAO);			
	}
	
	public FlightWaypointsModel()
	{
		
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
		return departure;
	}
	public Waypoint getArrivalLocation() 
	{
		return destination;
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
		this.departure = departureLocation;
	}
	public void setDepartureLocation(ImageView m, double lat, double lon)
	{
		this.departure = new Waypoint(m, lat, lon,0);
	}
	
	public void setArrivalLocation(Waypoint arrivalLocation) 
	{
		this.destination = arrivalLocation;
	}
	public void setArrivalLocation(ImageView m, double lat, double lon)
	{
		this.destination = new Waypoint(m, lat, lon,0);
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
	
	public String[] getAllLabels()
	{
		double[] latitudes = this.getAllWaypointLatitudes();
		double[] longitudes = this.getAllWaypointLongitudes();
		int length = latitudes.length;
		String[] labels = new String[length+2];
		labels[0] = this.getDepartureICAO();
		
		for(int i=1;i<length+1;i++)
		{
			labels[i] = Double.toString(latitudes[i-1]) +", " + Double.toString(longitudes[i-1]);
		}
		labels[length+1] = this.getDestinationICAO();
		return labels;
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

	public class Waypoint implements java.io.Serializable
	{
		private static final long serialVersionUID = -5807655103001199853L;
		private double latitude;
		private double longitude;
		private double altitude;
		private transient ImageView marker;
		
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
