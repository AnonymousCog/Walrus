package com.navlog.models;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
 

public class Frequencies 
{
	public class freqStruct
	{
		public String type;
		public String desc;
		public String freq;
	
		
		public String toString()
		{
			String out = "Type: " + type 
					+ "Description: " + desc 
					+ "Frequency: " + freq + "\n"; 
			return out;
		}
	} 

	
	
	private String ICAO="";
	public Frequencies()
	{
	}
	
	public ArrayList<freqStruct> getData(String ICAOcode, Context context)
	{
		ICAO = ICAOcode;
		freqStruct frq;
		ArrayList<freqStruct> lista = new ArrayList<freqStruct>();;
        Boolean outCode = false;
		
		String csvFile = "data/frequencies.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try 
		{
			InputStream is;
			is = context.getAssets().open (csvFile);
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while ((line = br.readLine()) != null) 
			{
				String[] values = line.split(",");
				if(ICAO.equals(values[0]))
				{
					frq = new freqStruct();
					frq.type = values[1];
					frq.desc = values[2];
					frq.freq = values[3];
					lista.add(frq);

					do
					{
						line = br.readLine();
						values = line.split(",");
						if (ICAO.equals(values[0]))
						{
							frq = new freqStruct();
							frq.type = values[1];
							frq.desc = values[2];
							frq.freq = values[3];
							lista.add(frq);
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