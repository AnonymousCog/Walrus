package com.navlog.models;

import java.util.ArrayList;

import java.util.List;


public class LegDataEntry implements java.io.Serializable
{		
	/**
	 * 
	 */
	private static final long serialVersionUID = -5870182341910414474L;
	/**
	 * data es un Mapa con el total de la data. Tiene como llave el indice LEG de legDataEntry y como valor el mapa legDataEntry.
	 * La llave del mapa data es igual a la llave LEG de un valor dentro de legDataEntry. Esta variable es parte del constructor de
	 * Data Calculations y es necesario enviarla una vez este completa.
	 */
	private ArrayList<LegData> allLegData;
	private Boolean editable = true;
	
	/**
	 * Constructor clase para Data Entry para posterior uso de la ventana completa de calculos.
	 */
	public LegDataEntry()
	{
		allLegData = new ArrayList<LegData>();
	}
	
	public ArrayList<LegData> getLegDataList()
	{
		return this.allLegData;
	}
	
	public void setLegDataList(ArrayList<LegData> aLegs)
	{
		this.allLegData = aLegs;
	}
	
	
	public Boolean isEditable()
	{
		return editable;
	}
	public void setEditable(Boolean value)
	{
		editable = value;
	}
	
	
	public boolean addDataEntry(LegData aLeg)
	{
		if(!isNewEntryValid(aLeg.getLEG()))
			return false;

		allLegData.add(aLeg);
		return true;
	}
	
	
	/**
	 * Method to add a new legEntry.
	 * @param legIndex index.
	 * @param altitude Leg altitude.
	 * @param TAS Leg True Airspeed.
	 * @param rpms Leg RPMS.
	 * @param windSpeed Leg Winds Aloft Speed.
	 * @param windDir Leg Winds Aloft Direction.
	 * @param windTemp Leg Winds Aloft Temperature.
	 * @return True if succesfully added.
	 */

	public boolean addDataEntry(int legIndex, double altitude, double TAS, double rpms, double windSpeed, double windDir, double windTemp)
	{
		if(!isNewEntryValid(legIndex))
			return false;
		
		LegData aLeg = new LegData();
		aLeg.setLEG(legIndex);
		aLeg.setALT(altitude);
		aLeg.setTAS(TAS);
		aLeg.setRPM(rpms);
		aLeg.setSPD(windSpeed);
		aLeg.setDIR(windDir);
		aLeg.setTMP(windTemp);
		allLegData.add(aLeg);
		return true;
	}
	
	
	public void updateLeg(LegData aLeg)
	{
		int legIndex = aLeg.getLEG();
		if(!this.isUpdateValid(legIndex))
			return;		
		
		allLegData.set(legIndex, aLeg);
	}
		
	/**
	 * Method to update a legEntry
	 * @param legIndex Leg index to be modified.
	 * @param altitude new Leg Altitude.
	 * @param TAS new Leg TAS.
	 * @param rpms new Leg RPMS.
	 * @param windSpeed new Leg Winds Aloft Speed.
	 * @param windDir new Leg Winds Aloft Direction.
	 * @param windTemp new Leg Winds Aloft Temperature.
	 */
	public void updateLeg(int legIndex, double altitude, double TAS, double rpms, double windSpeed, double windDir, double windTemp)
	{
		if(!this.isUpdateValid(legIndex))
			return;
		LegData aLeg = this.allLegData.get(legIndex);
		
		aLeg.setLEG(legIndex);
		aLeg.setALT(altitude);
		aLeg.setTAS(TAS);
		aLeg.setRPM(rpms);
		aLeg.setSPD(windSpeed);
		aLeg.setDIR(windDir);
		aLeg.setTMP(windTemp);
		allLegData.set(legIndex, aLeg);
	}
	
	/**
	 * Method to delete a legEntry 
	 * @param legIndex Leg index to be deleted.
	 */
	public void deleteDataEntry(int legIndex)
	{
		allLegData.remove(legIndex);
	}
	
	/**
	 * Method to determine if a new entry is valid or not using by using leg Index sent by the interface.
	 * @param index Leg Index to evaluate
	 * @return True if valid.
	 */	
	private boolean isNewEntryValid(int index)
	{
		for(int i=0;i<allLegData.size();i++)
		{
			if(allLegData.get(i).getLEG()==index)
			{
				return false;
			}
		}
		return true;
	}
	
	private boolean isUpdateValid(int index)
	{
		for(int i=0;i<allLegData.size();i++)
		{
			if(allLegData.get(i).getLEG()==index)
			{
				return true;
			}
		}
		return false;
	}
}


