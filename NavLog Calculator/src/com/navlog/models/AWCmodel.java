package com.navlog.models;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;


public class AWCmodel 
{
	public void test()
	{
		try 
		{
			
			String uri =String.format("http://www.aviationweather.gov/adds/dataserver_current/httpparam?dataSource=metars&"+
						"requestType=retrieve&format=xml&mostRecentForEachStation=constraint&hoursBeforeNow=5&stationString={0},{1}","TJSJ", "TJMZ");
				    
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
}
