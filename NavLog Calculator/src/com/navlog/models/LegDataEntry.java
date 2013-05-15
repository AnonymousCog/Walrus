package com.navlog.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegDataEntry
{		
	/**
	 * data es un Mapa con el total de la data. Tiene como llave el indice LEG de legDataEntry y como valor el mapa legDataEntry.
	 * La llave del mapa data es igual a la llave LEG de un valor dentro de legDataEntry. Esta variable es parte del constructor de
	 * Data Calculations y es necesario enviarla una vez este completa.
	 */
	public List<legData> allLegData;
	
	/**
	 * Constructor clase para Data Entry para posterior uso de la ventana completa de calculos.
	 */
	public LegDataEntry()
	{
		
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
	@SuppressWarnings("unchecked")
	public boolean addDataEntry(int legIndex, double altitude, double TAS, double rpms, double windSpeed, double windDir, double windTemp)
	{
		if(!isNewEntryValid(legIndex))
			return false;
		
		legData aLeg = new legData();
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
		legData aLeg = this.allLegData.get(legIndex);
		
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

class legData
{
	private int LEG;
	private double ALT;
	private double RPM;
	private double TAS;
	private double DIR;
	private double SPD;
	private double TMP;
	private double TC;
	private double WCA;
	private double TH;
	private double VAR;
	private double MH;
	private double LEG_DIST;
	private double LEG_DIST_REM;
	private double EST_GS;
	private double ACT_GS;
	private double ETE;
	private double ATE;
	private double ATA;
	private double ETA;
	private double LEG_FUEL;
	private double LEG_FUEL_REM;
	public int getLEG() {
		return LEG;
	}
	public void setLEG(int lEG) {
		LEG = lEG;
	}
	public double getALT() {
		return ALT;
	}
	public void setALT(double aLT) {
		ALT = aLT;
	}
	public double getRPM() {
		return RPM;
	}
	public void setRPM(double rPM) {
		RPM = rPM;
	}
	public double getTAS() {
		return TAS;
	}
	public void setTAS(double tAS) {
		TAS = tAS;
	}
	public double getDIR() {
		return DIR;
	}
	public void setDIR(double dIR) {
		DIR = dIR;
	}
	public double getSPD() {
		return SPD;
	}
	public void setSPD(double sPD) {
		SPD = sPD;
	}
	public double getTMP() {
		return TMP;
	}
	public void setTMP(double tMP) {
		TMP = tMP;
	}
	public double getTC() {
		return TC;
	}
	public void setTC(double tC) {
		TC = tC;
	}
	public double getWCA() {
		return WCA;
	}
	public void setWCA(double wCA) {
		WCA = wCA;
	}
	public double getTH() {
		return TH;
	}
	public void setTH(double tH) {
		TH = tH;
	}
	public double getVAR() {
		return VAR;
	}
	public void setVAR(double vAR) {
		VAR = vAR;
	}
	public double getMH() {
		return MH;
	}
	public void setMH(double mH) {
		MH = mH;
	}
	public double getLEG_DIST() {
		return LEG_DIST;
	}
	public void setLEG_DIST(double lEG_DIST) {
		LEG_DIST = lEG_DIST;
	}
	public double getLEG_DIST_REM() {
		return LEG_DIST_REM;
	}
	public void setLEG_DIST_REM(double lEG_DIST_REM) {
		LEG_DIST_REM = lEG_DIST_REM;
	}
	public double getEST_GS() {
		return EST_GS;
	}
	public void setEST_GS(double eST_GS) {
		EST_GS = eST_GS;
	}
	public double getACT_GS() {
		return ACT_GS;
	}
	public void setACT_GS(double aCT_GS) {
		ACT_GS = aCT_GS;
	}
	public double getETE() {
		return ETE;
	}
	public void setETE(double eTE) {
		ETE = eTE;
	}
	public double getATE() {
		return ATE;
	}
	public void setATE(double aTE) {
		ATE = aTE;
	}
	public double getATA() {
		return ATA;
	}
	public void setATA(double aTA) {
		ATA = aTA;
	}
	public double getETA() {
		return ETA;
	}
	public void setETA(double eTA) {
		ETA = eTA;
	}
	public double getLEG_FUEL() {
		return LEG_FUEL;
	}
	public void setLEG_FUEL(double lEG_FUEL) {
		LEG_FUEL = lEG_FUEL;
	}
	public double getLEG_FUEL_REM() {
		return LEG_FUEL_REM;
	}
	public void setLEG_FUEL_REM(double lEG_FUEL_REM) {
		LEG_FUEL_REM = lEG_FUEL_REM;
	}	
}
