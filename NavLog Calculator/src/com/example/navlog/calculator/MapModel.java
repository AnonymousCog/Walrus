/*
 * Data to be contained in FLIGHT MODEL
 * 	Departude airport coordinates
 * 	Destination airport coodinates
 * 	all waypoint coordinates in a stack
 *	current map coordinates
 * 
 */


package com.example.navlog.calculator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Boolean;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;




public class MapModel 
{
	private String mapFileName;
	private String mapType;
	private boolean downloaded;
	private Date expiration;
	private double northBound;
	private double southBound;
	private double eastBound;
	private double westBound;
	private int mapPxWidth;
	private int mapPxHeight;
	private int tilePxWidth;
	private int tilePxHeight;
	private String TAG = "MapModel";
	private static final String filePath = "data/Maps.xml";
	
	
	
	public MapModel()
	{
		
	}
	
	
	public boolean parseXML(Context appContext,String mapName) throws ParserConfigurationException, ParseException
	{
		boolean found = false;
		InputStream stream = null;
		try
		{
			
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			AssetManager manager = appContext.getAssets();
			stream = manager.open(filePath);
			Document doc = dBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("map");
			
			for(int i = 0; i < nList.getLength(); i++)
			{
				Node node = nList.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element) node;
					String nMapName = getTagValue("name", element);
					if(nMapName.equals(mapName))
					{
						this.setMapFileName(getTagValue("name", element));
						this.setMapType(getTagValue("type", element));
						this.setDownloaded(Boolean.parseBoolean(getTagValue("downloaded", element)));
						
						this.setNorthBound(Double.parseDouble( getTagValue("north", element)));
						this.setSouthBound(Double.parseDouble( getTagValue("south", element)));
						this.setEastBound(Double.parseDouble( getTagValue("east", element)));
						this.setWestBound(Double.parseDouble( getTagValue("west", element)));
						this.setMapPxWidth(Integer.parseInt(getTagValue("mapPxWidth", element)));
						this.setMapPxHeight(Integer.parseInt(getTagValue("mapPxHeight", element)));
						this.setTilePxWidth(Integer.parseInt(getTagValue("tilePxWidth", element)));
						this.setTilePxHeight(Integer.parseInt(getTagValue("tilePxHeight", element)));
						
						
						Date temp = new SimpleDateFormat("MM/dd/yy").parse(getTagValue("expiration", element));
						this.setExpiration(temp);
						found = true;
						break;
					}
									
				}
			}
			
		}
		catch(IOException e)
		{
			Log.d(TAG, e.getMessage()); 
			
		}
		catch(SAXException e)
		{
			Log.d(TAG, e.getMessage().toString()); 
		}
		finally
		{
			
			try 
			{
				stream.close();
				
				
			} 
			catch (IOException e) 
			{
				Log.d(TAG, e.getMessage()); 
			}
		}
			
		
		
		return found;
	}
	
	//can get atribute values inside any tag
	private String getAttributeValue(String sTag,String sAttribute, Element eElement)
	{
		
		String content;
		NodeList nlList = eElement.getElementsByTagName(sTag);
		Element attribute = (Element) nlList.item(0);
		content = attribute.getAttribute(sAttribute);
		return content;
	}
	
	
	
 
	  private static String getTagValue(String sTag, Element eElement) 
	  {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	 
	    Node nValue = nlList.item(0);
	 
		return nValue.getNodeValue();
	  }
	  
	public boolean isMapExpired()
	{
		boolean expired;
		Date now = new Date();
		
		expired = now.after(expiration);
		return expired;
	}


	public String getMapFileName() {
		return mapFileName;
	}


	public String getMapType() {
		return mapType;
	}


	public boolean isDownloaded() {
		return downloaded;
	}


	public Date getExpiration() {
		return expiration;
	}


	public double getNorthBound() {
		return northBound;
	}


	public double getSouthBound() {
		return southBound;
	}


	public double getEastBound() {
		return eastBound;
	}


	public double getWestBound() {
		return westBound;
	}


	public int getMapPxWidth() {
		return mapPxWidth;
	}


	public int getMapPxHeight() {
		return mapPxHeight;
	}


	public int getTilePxWidth() {
		return tilePxWidth;
	}


	public int getTilePxHeight() {
		return tilePxHeight;
	}


	public String getTAG() {
		return TAG;
	}


	public static String getFilepath() {
		return filePath;
	}


	private void setMapFileName(String mapFileName) {
		this.mapFileName = mapFileName;
	}


	private void setMapType(String mapType) {
		this.mapType = mapType;
	}


	private void setDownloaded(boolean downloaded) {
		this.downloaded = downloaded;
	}


	private void setExpiration(Date expiration) {
		this.expiration = expiration;
	}


	private void setNorthBound(double northBound) {
		this.northBound = northBound;
	}


	private void setSouthBound(double southBound) {
		this.southBound = southBound;
	}


	private void setEastBound(double eastBound) {
		this.eastBound = eastBound;
	}


	private void setWestBound(double westBound) {
		this.westBound = westBound;
	}


	private void setMapPxWidth(int mapPxWidth) {
		this.mapPxWidth = mapPxWidth;
	}


	private void setMapPxHeight(int mapPxHeight) {
		this.mapPxHeight = mapPxHeight;
	}


	private void setTilePxWidth(int tilePxWidth) {
		this.tilePxWidth = tilePxWidth;
	}


	private void setTilePxHeight(int tilePxHeight) {
		this.tilePxHeight = tilePxHeight;
	}





	
	
	
	


	

}

