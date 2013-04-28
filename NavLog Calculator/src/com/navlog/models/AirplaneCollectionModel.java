package com.navlog.models;

import java.util.HashMap;
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
		String[] labels =(String[]) profiles.keySet().toArray();	
		return labels;
	}

}
