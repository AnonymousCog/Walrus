package com.navlog.models;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.navlog.models.FlightModel.Waypoint;

public class AviationWeatherModel 
{
	/**
	 * Lista que tiene todas las estaciones localizadas para la clase Flightmodel.
	 */	
	private List<WeatherStation> listaAWCs;
	
	/**
	 * En el constructor se conecta a http://www.aviationweather.gov y se baja toda la data necesaria.
	 * @param incInfo Recibe la clase Flightmodel con todos los waypoints ademas de los puntos de salida y llegada.
	 * Tambien debe incluir los ICAO para ambos aeropuertos. 
	 *
	 */
	public AviationWeatherModel(FlightModel incInfo)
	{
		List<String> allWeatherStations = null;	
		allWeatherStations.add(incInfo.getDepartureICAO());		
		allWeatherStations.add(incInfo.getDestinationICAO());		
		calculateMetars(allWeatherStations);
	}
	
	public List<WeatherStation> getAWCData()
	{
		return listaAWCs;
	}
	
	/**
	 * Metodo para ejecutar el retrieve METARS del website de AWC.
	 * LLenara todos las propiedades de la clase.
	 */
	private void calculateMetars(List<String> allWeatherStations)
	{
		try 
		{
			String listaStations="";
			for(int i=0;i<allWeatherStations.size();i++)
			{
				listaStations += String.format("{0},", allWeatherStations.get(i));
			}
			
			String uri =String.format("http://www.aviationweather.gov/adds/dataserver_current/httpparam?dataSource=metars"+
			"&requestType=retrieve&format=xml&mostRecentForEachStation=constraint&hoursBeforeNow=5&stationString={0}"+
			"&fields=station_id,elevation_m,observation_time,temp_c,wind_dir_degrees,wind_speed_kt,visibility_statute_mi",listaStations);
				    
			URL url = new URL(uri);
			HttpURLConnection connection =
			    (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/xml");

			InputStream xml = connection.getInputStream();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xml);
			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("METAR");
			
			WeatherStation aAWC;
			for(int i = 0; i < nList.getLength(); i++)
			{
				aAWC = new WeatherStation();
				Node node = nList.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element) node;
					aAWC.setICAO(getTagValue("station_id", element));
					aAWC.setElevation(Double.parseDouble(getTagValue("elevation_m", element)));
					aAWC.setAtisCode("N/A");
					aAWC.setObservationTime(getTagValue("observation_time", element));
					aAWC.setTemperature(Double.parseDouble(getTagValue("temp_c", element)));
					aAWC.setWindDirection(Double.parseDouble(getTagValue("wind_dir_degrees", element)));
					aAWC.setWindSpeed(Double.parseDouble(getTagValue("wind_speed_kt", element)));
					aAWC.setVisibility(Double.parseDouble(getTagValue("visibility_statute_mi", element)));
					
					//<sky_condition sky_cover="FEW" cloud_base_ft_agl="4200"/>
					//TODO CONDITIONS
					
					this.listaAWCs.add(aAWC);
				}
			}						
		} 
		catch (Exception e) 
		{
            System.out.println("XML Passing Exception = " + e);
        }		
	}
		 
	private static String getTagValue(String sTag, Element eElement) 
	{
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	    Node nValue = nlList.item(0);
		return nValue.getNodeValue();
	}	
}

class WeatherStation 
{
	private String ICAO;
	private String atisCode;
	private String observationTime;
	private double elevation;
	private double visibility;
	private double windSpeed;
	private double windDirection;
	private double temperature;
	private List<String> skyConditions;
	public String getICAO() {
		return ICAO;
	}
	public void setICAO(String iCAO) {
		ICAO = iCAO;
	}
	public String getAtisCode() {
		return atisCode;
	}
	public void setAtisCode(String atisCode) {
		this.atisCode = atisCode;
	}
	public double getElevation() {
		return elevation;
	}
	public void setElevation(double elevation) {
		this.elevation = elevation;
	}
	public String getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(String observationTime) {
		this.observationTime = observationTime;
	}
	public double getVisibility() {
		return visibility;
	}
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public double getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(double windDirection) {
		this.windDirection = windDirection;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public List<String> getSkyConditions() {
		return skyConditions;
	}
	public void setSkyConditions(List<String> skyConditions) {
		this.skyConditions = skyConditions;
	}
			
}