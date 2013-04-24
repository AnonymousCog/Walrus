package com.navlog.models;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;



public class AviationWeatherModel 
{
	private FlightModel flightInfo;
	private String[] waypointsICAOS;
	
	public AviationWeatherModel(FlightModel incInfo)
	{
		flightInfo = incInfo;
	}
	
	private void calculateMetar()
	{
		try 
		{
			String uri =String.format("http://www.aviationweather.gov/adds/dataserver_current/httpparam?dataSource=metars&"+
						"requestType=retrieve&format=xml&mostRecentForEachStation=constraint&hoursBeforeNow=5&stationString={0},{1}",flightInfo.getDepartureICAO(),flightInfo.getDestinationICAO());
				    
			URL url = new URL(uri);
			HttpURLConnection connection =
			    (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/xml");

			InputStream xml = connection.getInputStream();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xml);
		} 
		catch (Exception e) 
		{
            System.out.println("XML Passing Exception = " + e);
        }
	}
	
	private String getClosestWeatherStation(double lat, double lon)
	{
		String uri =String.format("http://www.aviationweather.gov/adds/dataserver_current/httpparam?dataSource="+
					"stations&requestType=retrieve&format=xml&radialDistance=10;{0},{1}&fields=station_id",lon,lat);
		try
		{	
		URL url = new URL(uri);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		}
		catch(Exception e)
		{
			System.out.println("XML Passing Exceptoin =" +e);
		}
		return uri;
	}
	
	private double[] AtisCodes;
	public double[] getATISCode()
	{
		return AtisCodes;
	}
	
	private double[] Elevations;
	public double[] getElevations()
	{
		return Elevations;
	}
	
	private double[] ObservationTimes;
	public double[] getObservationTime()
	{
		return ObservationTimes;
	}
	
	private double[] Visibilities;
	public double[] getVisitilities()
	{
		return Visibilities;	
	}
	
	private double[]WindSpeeds;
	public double[] getWindSpeeds()
	{
		return WindSpeeds;
	}
	
	private double[]WindDirs;
	public double[] getWindDirections()
	{
		return WindDirs;
	}
	
	private double[]Altitudes;
	public double[] getAltitudes()
	{
		return Altitudes;
	}
	
	private double[]Temperatures;
	public double[] getTemperatures()
	{
		return Temperatures;
	}
	
	private double[]DepartureSkyConditions;
	public double[] getDepartureConditions()
	{
		return DepartureSkyConditions;
	}
	
	private double[] DestinationSkyConditions;
	public double[] getDestinationConditions()
	{
		return DestinationSkyConditions;
	}
	
	
}
