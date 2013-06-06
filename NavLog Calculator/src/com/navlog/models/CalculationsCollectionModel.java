package com.navlog.models;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;

public class CalculationsCollectionModel implements java.io.Serializable  
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3613233370564016526L;
	private static final String fileName = "ColectionList.ser";
	private Map<String,CalculationsModel> flights;
	public static final String tempValue = "temp";
	
	public CalculationsCollectionModel()
	{
		flights = new HashMap<String,CalculationsModel>();
		
	}
	
	public void addCalculationModel(String label, CalculationsModel calc )
	{
		flights.put(label, calc);
	}
	public CalculationsModel getCalculationsModel(String label)
	{
		return flights.get(label);
	}
	public void removeCalculationsModel(String label)
	{
		flights.remove(label);
	}
	public String[] getAllLabels()
	{
		int size = flights.size();
		int index = 0;
		String[] labels = new String[size];
		Iterator<Map.Entry<String ,CalculationsModel>> entries = flights.entrySet().iterator();
		while (entries.hasNext()) 
		{
		    Map.Entry<String ,CalculationsModel> entry = entries.next();
		    String label = entry.getKey();
		    if(!label.equals(tempValue))
		    {
		    	labels[index] = entry.getKey();
		    	index++;
		    }
		}
		return labels;
		
	}
	
	public Boolean containsKey(String label)
	{
		Boolean contains;
		
		contains  = this.flights.containsKey(label);
		
		return contains;
	}
	public int getSize()
	{
		int size;
		size = flights.size();
		return size;
	}
	
	public static CalculationsCollectionModel loadCalculationsList(Context context)
	{
		FileInputStream fis;
		CalculationsCollectionModel flightList = new CalculationsCollectionModel();
		try 
		{
			fis = context.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			flightList = (CalculationsCollectionModel) is.readObject();
			is.close();
			return flightList;
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flightList;
		
	}
	
	public void saveCalculationsList(Context context)
	{
		
		FileOutputStream fos;
		try 
		{
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(this);
			os.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	
	

}
