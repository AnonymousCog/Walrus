package com.navlog.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AirplaneCollectionModel implements java.io.Serializable 
{

	
	/**
	 * 
	 */
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
