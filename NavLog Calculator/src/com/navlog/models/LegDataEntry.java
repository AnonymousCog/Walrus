package com.navlog.models;

import java.util.HashMap;
import java.util.Map;

public class LegDataEntry<aDataEntry> {
	
	/**
	 * legDataEntry Variable tipo Mapa. Contiene llave como string y su valor como double.
	 * Las llaves que podemos encontrar son: LEG, ALT, RPM, TAS, DIR, SPD y TMP.
	 */
	public Map<String, Double> aDataEntry;	
	
	/**
	 * data es un Mapa con el total de la data. Tiene como llave el indice LEG de legDataEntry y como valor el mapa legDataEntry.
	 * La llave del mapa data es igual a la llave LEG de un valor dentro de legDataEntry. Esta variable es parte del constructor de
	 * Data Calculations y es necesario enviarla una vez este completa.
	 */
	public Map<Double, aDataEntry> allLegDataEntry;
	
	private double counter;
	
	/**
	 * Constructor clase para Data Entry para posterior uso de la ventana completa de calculos.
	 */
	public LegDataEntry()
	{
		counter=0;
		allLegDataEntry = new HashMap<Double, aDataEntry>();
	}
	
	/**
	 * Method to add a new legEntry.
	 * @param altitude Leg altitude.
	 * @param TAS Leg True Airspeed.
	 * @param rpms Leg RPMS.
	 * @param windSpeed Leg Winds Aloft Speed.
	 * @param windDir Leg Winds Aloft Direction.
	 * @param windTemp Leg Winds Aloft Temperature.
	 */
	@SuppressWarnings("unchecked")
	public void addDataEntry(double altitude, double TAS, double rpms, double windSpeed, double windDir, double windTemp)
	{
		counter++;
		aDataEntry = new HashMap<String, Double>();
		aDataEntry.put("LEG", counter);
		aDataEntry.put("ALT", altitude);
		aDataEntry.put("RPM", rpms);
		aDataEntry.put("TAS", TAS);
		aDataEntry.put("DIR", windDir);
		aDataEntry.put("SPD", windSpeed);
		aDataEntry.put("TMP", windTemp);
		
		allLegDataEntry.put(counter, (aDataEntry) aDataEntry);
	}
		
	/**
	 * Method to update a legEntry
	 * @param LEG Leg index to be modified.
	 * @param altitude new Leg Altitude.
	 * @param TAS new Leg TAS.
	 * @param rpms new Leg RPMS.
	 * @param windSpeed new Leg Winds Aloft Speed.
	 * @param windDir new Leg Winds Aloft Direction.
	 * @param windTemp new Leg Winds Aloft Temperature.
	 */
	@SuppressWarnings("unchecked")
	public void updateLeg(int LEG, double altitude, double TAS, double rpms, double windSpeed, double windDir, double windTemp)
	{
		aDataEntry = new HashMap<String, Double>();
		aDataEntry.put("LEG", (double) LEG);
		aDataEntry.put("ALT", altitude);
		aDataEntry.put("RPM", rpms);
		aDataEntry.put("TAS", TAS);
		aDataEntry.put("DIR", windDir);
		aDataEntry.put("SPD", windSpeed);
		aDataEntry.put("TMP", windTemp);
		allLegDataEntry.put((double) LEG, (aDataEntry) aDataEntry);
		
	}
	/**
	 * Method to delete a legEntry 
	 * @param LEG Leg index to be deleted.
	 */
	public void deleteDataEntry(int LEG)
	{
		allLegDataEntry.remove(LEG);
	}
}
