package com.navlog.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AirplaneProfileModel implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6939222492100483403L;
	private String airplaneName;
	private String airplaneBrand;
	private String airplaneModel;
	private String airplaneTailNumber;
	private Map<String ,CruisePerformanceModel> performanceParams = new HashMap<String,CruisePerformanceModel>();
	
	public AirplaneProfileModel()
	{
		
	}
	
	
	public void addCruisePerformanceParam(String label,CruisePerformanceModel param)
	{
		performanceParams.put(label, param);
		
	}
	
	public void removeCruisePerformanceParam(String label)
	{
		performanceParams.remove(label);
	}
	public CruisePerformanceModel getCruisePerformanceParam(String label)
	{
		CruisePerformanceModel mod = new CruisePerformanceModel();
		if(performanceParams.containsKey(label))
		{
			mod = performanceParams.get(label);
		}
			return mod;
	}
	
	
	public double[] getAllAltitudes()
	{
		String[] labels = getAllLabels();
		int size = labels.length;
		double[] alts = new double[size];
		for(int i=0;i<size;i++)
		{
			alts[i] = performanceParams.get(labels[i]).getAltitude();
		}
		return alts;
	}

	
	public double[] getAllRpms()
	{
		String[] labels = getAllLabels();
		int size = labels.length;
		double[] rpms = new double[size];
		for(int i=0;i<size;i++)
		{
			rpms[i] = performanceParams.get(labels[i]).getRpm();
		}
		return rpms;
	}

	
	public double[] getAllBelow20cKTAS()
	{
		String[] labels = getAllLabels();
		int size = labels.length;
		double[] KTAS = new double[size];
		for(int i=0;i<size;i++)
		{
			KTAS[i] = performanceParams.get(labels[i]).getBelow20Ktas();
		}
		return KTAS;
	}
	
	public double[] getAllAbove20cKTAS()
	{
		String[] labels = getAllLabels();
		int size = labels.length;
		double[] KTAS = new double[size];
		for(int i=0;i<size;i++)
		{
			KTAS[i] = performanceParams.get(labels[i]).getAbove20Ktas();
		}
		return KTAS;
	}
	
	public double[] getAllStdKTAS()
	{
		String[] labels = getAllLabels();
		int size = labels.length;
		double[] KTAS = new double[size];
		for(int i=0;i<size;i++)
		{
			KTAS[i] = performanceParams.get(labels[i]).getStd20Ktas();
		}
		return KTAS;
	}
	
	public double[] getAllBelow20cGPH()
	{
		String[] labels = getAllLabels();
		int size = labels.length;
		double[] gph = new double[size];
		for(int i=0;i<size;i++)
		{
			gph[i] = performanceParams.get(labels[i]).getBelow20Gph();
		}
		return gph;
	}
	
	public double[] getAllAbove20cGPH()
	{
		String[] labels = getAllLabels();
		int size = labels.length;
		double[] gph = new double[size];
		for(int i=0;i<size;i++)
		{
			gph[i] = performanceParams.get(labels[i]).getAbove20Gph();
		}
		return gph;
	}
	
	public double[] getAllstdGPH()
	{
		String[] labels = getAllLabels();
		int size = labels.length;
		double[] gph = new double[size];
		for(int i=0;i<size;i++)
		{
			gph[i] = performanceParams.get(labels[i]).getStd20Gph();
			
		}
		return gph;
	}
	
	
	public String[] getAllLabels()
	{
		int size = performanceParams.size();
		int index = 0;
		String[] labels = new String[size];
		Iterator<Map.Entry<String ,CruisePerformanceModel>> entries = performanceParams.entrySet().iterator();
		while (entries.hasNext()) 
		{
		    Map.Entry<String ,CruisePerformanceModel> entry = entries.next();
		    labels[index] = entry.getKey();
		    index++;
		}
		return labels;
	}
	
	
	
	public AirplaneProfileModel(String name, String brand, String model, String tailNum)
	{
		this.setAirplaneName(name);
		this.setAirplaneBrand(brand);
		this.setAirplaneModel(model);
		this.setAirplaneTailNumber(tailNum);
	}
	
	public String getAirplaneName() {
		return airplaneName;
	}
	public void setAirplaneName(String airplaneName) {
		this.airplaneName = airplaneName;
	}
	public String getAirplaneBrand() {
		return airplaneBrand;
	}
	public void setAirplaneBrand(String airplaneBrand) {
		this.airplaneBrand = airplaneBrand;
	}
	public String getAirplaneModel() {
		return airplaneModel;
	}
	public void setAirplaneModel(String airplaneModel) {
		this.airplaneModel = airplaneModel;
	}
	public String getAirplaneTailNumber() {
		return airplaneTailNumber;
	}
	public void setAirplaneTailNumber(String airplaneTailNumber) {
		this.airplaneTailNumber = airplaneTailNumber;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getLabel()
	{
		String displayName;
		displayName = this.getAirplaneName() +" - " 
					+ this.getAirplaneBrand() +" - "
					+ this.getAirplaneModel();
		
		return displayName;
	}
	
	
		

}
