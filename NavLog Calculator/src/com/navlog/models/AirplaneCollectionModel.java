package com.navlog.models;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;

public class AirplaneCollectionModel implements java.io.Serializable 
{

	
	/**
	 * 
	 */
	public static final String fileName = "AirplaneList.ser";
	private static final long serialVersionUID = -8448042904958822051L;
	private Map<String ,AirplaneProfileModel> profiles = new HashMap<String,AirplaneProfileModel>();
	
	public AirplaneCollectionModel()
	{
		
	}
	
	public void addAirplaneProfile(String label, AirplaneProfileModel profile)
	{
		profiles.put(label, profile);
	}
	public void removeAirplaneProfile(String label)
	{
		profiles.remove(label);
	}
	
	public static AirplaneCollectionModel loadAirplaneList(Context context)
	{
		FileInputStream fis;
		AirplaneCollectionModel airplaneList = new AirplaneCollectionModel();
		try 
		{
			fis = context.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			airplaneList = (AirplaneCollectionModel) is.readObject();
			is.close();
			return airplaneList;
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return airplaneList;
		
	}
	
	public void saveAirplaneList(Context context)
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
	
	
	public AirplaneProfileModel getAirplaneProfile(String label)
	{
		AirplaneProfileModel ap = new AirplaneProfileModel();
		if(profiles.containsKey(label))
		{
			ap = profiles.get(label);
		}
		return ap;
	}
	
	public String[] getAllLabels()
	{
		int size = profiles.size();
		int index = 0;
		String[] labels = new String[size];
		Iterator<Map.Entry<String ,AirplaneProfileModel>> entries = profiles.entrySet().iterator();
		while (entries.hasNext()) 
		{
		    Map.Entry<String ,AirplaneProfileModel> entry = entries.next();
		    labels[index] = entry.getKey();
		    index++;
		}
		return labels;
	}
	
	public int getSize()
	{
		int size;
		size = profiles.size();
		return size;
	}

}
