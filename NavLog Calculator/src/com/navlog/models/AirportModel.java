/*
 *   `Num` int(5) NOT NULL AUTO_INCREMENT,
  `Airport` varchar(70) NOT NULL,
  `Town` varchar(70) NOT NULL,
  `Country` varchar(70) NOT NULL,
  `IATA` varchar(3) NOT NULL,
  `ICAO` varchar(4) NOT NULL,
  `Latitude` decimal(10,8) NOT NULL,
  `Longitude` decimal(11,8) NOT NULL,
  `Elevation` int(6) NOT NULL,
  `UTC` varchar(7) NOT NULL,
  `Letter` varchar(2) DEFAULT NULL,
(2816,'Luis Munoz Marin Intl','San Juan','Puerto Rico','SJU','TJSJ',18.43941700,-66.00183300,9,'-4','U\r')
 num    airport                 town         country   IATA  ICAO
 */

//Airport indexing fails on and after airport id 188 and I dont know why
//ucs on 188 was an double -3.5 instead of int -3

package com.navlog.models;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;



public class AirportModel
{
	private String name;
	private String IATA;
	private String ICAO;
	private double latitude;
	private double longitude;
	private static final String ns = null; //ns -> namespace
	private String TAG = "MapModel";
	private static final String filePath = "data/Airports.xml";
		
	public AirportModel()
	{
		
	}
	
	public AirportModel(AirportModel a)
	{
		copyAirport(a);
	}
	
	public void copyAirport(AirportModel a)
	{
		setName(a.getName());
		setIATA(a.getIATA());
		setICAO(a.getICAO());
		setLatitude(a.getLatitude());
		setLongitude(a.getLongitude());
		
		
	}
	
	public boolean parseAirportXML(Context appContext, String ICAO)
	{
		this.setICAO(ICAO);
		boolean found = false;
		InputStream in = null;
		
		try
		{
			AssetManager manager = appContext.getAssets();
			in = manager.open(filePath);
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			found = readDocument(parser);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.getMessage().toString()); 
		}
			
		finally
		{
			
			try 
			{
				in.close();
				
				
			} 
			catch (IOException e) 
			{
				Log.d(TAG, e.getMessage()); 
			}
		}
		return found;
		
	}
	
	private boolean readDocument(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		AirportModel data = new AirportModel();
		boolean found = false;
		
		parser.require(XmlPullParser.START_TAG, ns, "AIRPORTS");
		while(parser.next() != XmlPullParser.END_TAG)
		{
			if(parser.getEventType() != XmlPullParser.START_TAG)
			{
				continue;
			}
			String label = parser.getName();
			if(label.equals("AIRPORT"))
			{
				data = readAirport(parser);

				if(data.getICAO().equals(this.getICAO()))
				{
					
					copyAirport(data);
					found = true;
					return true;
				}
			}
			else
			{
				skip(parser);
			}
			
		}
		return found;
		
	}
	
	private AirportModel readAirport(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "AIRPORT");
		AirportModel data = new AirportModel();
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG) 
			{
		            continue;
			}
			String child = parser.getName();
			if(child.equals("NAME"))
			{
				data.setName(readChild(parser, "NAME"));
			}
		
			else if(child.equals("IATA"))
			{
				data.setIATA(readChild(parser, "IATA"));
			}
			else if(child.equals("ICAO"))
			{
				data.setICAO(readChild(parser, "ICAO"));
			}
			else if(child.equals("LATITUDE"))
			{	
				String lat = readChild(parser, "LATITUDE");
				double dlat = Double.parseDouble(lat);
				data.setLatitude(dlat);
			}
			else if(child.equals("LONGITUDE"))
			{	
				String lon = readChild(parser, "LONGITUDE");
				double dlon = Double.parseDouble(lon);
				data.setLongitude(dlon);
			}
			
			else
			{
				skip(parser);
			}
			
			
		}
		
		return data;
		
	}
	
	private String readChild(XmlPullParser parser, String tag) throws IOException, XmlPullParserException
	{
		parser.require(XmlPullParser.START_TAG, ns, tag);
		String text = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, tag);
		return text;
	}
	
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException
	{
		String result = "";
		 if (parser.next() == XmlPullParser.TEXT) 
		 {
		        result = parser.getText();
		        parser.nextTag();
		 }
		    return result;
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException 
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG) 
	    {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) 
	    {
	        switch (parser.next()) 
	        {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
	
	
	
	
	public void setName(String n)
	{
		name = n;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setIATA(String i)
	{
		IATA = i;
	}
	public String getIATA()
	{
		return IATA;
	}
	public void setICAO(String i)
	{
		ICAO = i;
	}
	public String getICAO()
	{
		return ICAO;
	}
	public void setLatitude(double lat)
	{
		latitude = lat;
	}
	public double getLatitude()
	{
		return latitude;
	}
	public void setLongitude(double lon)
	{
		longitude = lon;
	}
	public double getLongitude()
	{
		return longitude;
	}
	

}
