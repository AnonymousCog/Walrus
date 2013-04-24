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
	 * Constructor
	 * @param incInfo Recibe la clase Flightmodel con todos los waypoints ademas de
	 * los puntos de salida y llegada. Tambien debe incluir los ICAO para ambos aeropuertos.
	 * En el constructor se conecta a http://www.aviationweather.gov y se baja toda la data necesaria.
	 */
	public AviationWeatherModel(FlightModel incInfo)
	{
		List<String> allWeatherStations = null;	
		Waypoint aWaypoint;	
		List<Waypoint> points = incInfo.getPoints();		
		allWeatherStations.add(incInfo.getDepartureICAO());
		for(int i=0; i<points.size();i++)
		{
			aWaypoint = points.get(i);
			allWeatherStations.add(getClosestStation(aWaypoint.getLongitude(), aWaypoint.getLatitude()));
		}
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
	
	/**
	 * Metodo para buscar en el WAC la estacion metereologica mas cercana a las coordenadas.
	 * @param lat latitud del waypoint
	 * @param lon longitud del waypoint
	 * @return codigo de la estacion
	 */
	private String getClosestStation(double lat, double lon)
	{
		String uri =String.format("http://www.aviationweather.gov/adds/dataserver_current/httpparam?dataSource="+
					"stations&requestType=retrieve&format=xml&radialDistance=10;{0},{1}&fields=station_id",lon,lat);
		try
		{
			URL url = new URL(uri);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/xml");
	
			InputStream xml = connection.getInputStream();
	
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xml);			
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("Station");
			
			Node node = nList.item(0);
				
			if(node.getNodeType() == Node.ELEMENT_NODE)
			{
				Element element = (Element) node;
				return getTagValue("station_id", element);												
			}
		}
		catch(Exception e)
		{
			System.out.println("XML Passing Exceptoin =" +e);
		}
		return null;
	}
		 
	private static String getTagValue(String sTag, Element eElement) 
	{
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	    Node nValue = nlList.item(0);
		return nValue.getNodeValue();
	}	
}
