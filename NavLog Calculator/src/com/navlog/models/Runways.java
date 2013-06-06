package com.navlog.models;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
 

public class Runways 
{
	public class RunwayStruct
	{
		public int index;
		public String len;
		public String wid;
		public String material;
		public String light;
		public String runwayA;
		public String runwayB;
			
		public String toString()
		{
			String out = "Index: " + index + "\n"
					+ "Length: " + len + "\n"
					+ "Width: " + wid + "\n"
					+ "Material: "+ material + "\n"
					+ "Lights: "+ light + "\n"
					+ "Runway: "+ runwayA+"|"+runwayB +"\n"+"\n"; 
			return out;
		}
	} 

	
	
	private String ICAO="";
		
	public ArrayList<RunwayStruct> getData(String ICAOcode, Context context)
	{
		ICAO = ICAOcode;
		RunwayStruct runway;
		ArrayList<RunwayStruct> lista = new ArrayList<RunwayStruct>();;
        Boolean outCode = false;
		
		String csvFile = "data/runways.csv";
		BufferedReader br = null;
		String line = "";

		try 
		{
			InputStream is;
			int index=1;
			is = context.getAssets().open (csvFile);
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while ((line = br.readLine()) != null) 
			{
				String[] values = line.split(",");
				if(ICAO.equals(values[0].trim()))
				{
					runway = new RunwayStruct();
					runway.index = index;
					runway.len = values[1];
					runway.wid = values[2];
					runway.material = values[3];
					runway.light = values[4];
					runway.runwayA = values[5];
					runway.runwayB = values[6];
					lista.add(runway);
					index++;
					do
					{
						line = br.readLine();
						values = line.split(",");
						if (ICAO.equals(values[0]))
						{
							runway = new RunwayStruct();
							runway.index = index;
							runway.len = values[1];
							runway.wid = values[2];
							runway.material = values[3];
							runway.light = values[4];
							runway.runwayA = values[5];
							runway.runwayB = values[6];
							lista.add(runway);
							index++;
						}
						else { outCode = true; }
					} while (!outCode);
                }
				//return lista;
            }
			return lista;
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return lista;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return lista;
		} 
		finally 
		{
			if (br != null) 
			{
				try 
				{
					br.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
					return lista;
				}
			}
		}
	}
}