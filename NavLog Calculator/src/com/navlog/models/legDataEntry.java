package com.navlog.models;

import java.util.HashMap;
import java.util.Map;

public class legDataEntry {
	
	/**
	 * legDataEntry Variable tipo Mapa. Contiene llave como string y su valor como double.
	 * Las llaves que podemos encontrar son: LEG, ALT, RPM, TAS, DIR, SPD y TMP.
	 */
	private Map<String, Double> legDataEntry;	
	
	/**
	 * data es un Mapa con el total de la data. Tiene como llave el indice LEG de legDataEntry y como valor el mapa legDataEntry.
	 * La llave del mapa data es igual a la llave LEG de un valor dentro de legDataEntry. Esta variable es parte del constructor de
	 * Data Calculations y es necesario enviarla una vez este completa.
	 */
	public Map<Double, Map> legData;
	double counter;
	
	/**
	 * Constructor clase para Data Entry para posterior uso de la ventana completa de calculos.
	 */
	public legDataEntry()
	{
		counter=0;
		legData = new HashMap<Double, Map>();
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
	public void addDataEntry(double altitude, double TAS, double rpms, double windSpeed, double windDir, double windTemp)
	{
		counter++;
		legDataEntry = new HashMap<String, Double>();
		legDataEntry.put("LEG", counter);
		legDataEntry.put("ALT", altitude);
		legDataEntry.put("RPM", rpms);
		legDataEntry.put("TAS", TAS);
		legDataEntry.put("DIR", windDir);
		legDataEntry.put("SPD", windSpeed);
		legDataEntry.put("TMP", windTemp);
		
		legData.put(counter, legDataEntry);
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
	public void updateLeg(int LEG, double altitude, double TAS, double rpms, double windSpeed, double windDir, double windTemp)
	{
		legDataEntry = new HashMap<String, Double>();
		legDataEntry.put("LEG", (double) LEG);
		legDataEntry.put("ALT", altitude);
		legDataEntry.put("RPM", rpms);
		legDataEntry.put("TAS", TAS);
		legDataEntry.put("DIR", windDir);
		legDataEntry.put("SPD", windSpeed);
		legDataEntry.put("TMP", windTemp);
		legData.put((double) LEG, legDataEntry);
		
	}
	/**
	 * Method to delete a legEntry 
	 * @param LEG Leg index to be deleted.
	 */
	public void deleteDataEntry(int LEG)
	{
		legData.remove(LEG);
	}
}
