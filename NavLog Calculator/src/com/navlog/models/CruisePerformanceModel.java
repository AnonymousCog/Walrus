package com.navlog.models;

public class CruisePerformanceModel implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4902994798666609551L;
	private int altitude;
	private int rpm;
	private double below20Ktas;
	private double below20Gph;
	private double stdKtas;
	private double stdGph;
	private double above20Ktas;
	private double above20Gph;
	
	public CruisePerformanceModel()
	{
		
	}
	
	public CruisePerformanceModel(int alt, int r, double b20ktas,double b20gph,double sKtas, double sGph, double a20Ktas, double a20Gph)
	{
		this.setAltitude(alt);
		this.setRpm(r);
		this.setBelow20Ktas(b20ktas);
		this.setBelow20Gph(b20gph);
		this.setStd20Ktas(sKtas);
		this.setStd20Gph(sGph);
		this.setAbove20Ktas(a20Ktas);
		this.setAbove20Gph(a20Gph);
	}
	
	public String getLabel()
	{
		String label;
		String alt  = Integer.toString(this.getAltitude());
		String r = Integer.toString(this.getRpm());
		label = alt + " - " + rpm;
		return label;
	}
	public int getAltitude() {
		return altitude;
	}
	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}
	public int getRpm() {
		return rpm;
	}
	public void setRpm(int rpm) {
		this.rpm = rpm;
	}
	public double getBelow20Ktas() {
		return below20Ktas;
	}
	public void setBelow20Ktas(double below20Ktas) {
		this.below20Ktas = below20Ktas;
	}
	public double getBelow20Gph() {
		return below20Gph;
	}
	public void setBelow20Gph(double below20Gph) {
		this.below20Gph = below20Gph;
	}
	public double getStd20Ktas() {
		return stdKtas;
	}
	public void setStd20Ktas(double std20Ktas) {
		this.stdKtas = std20Ktas;
	}
	public double getStd20Gph() {
		return stdGph;
	}
	public void setStd20Gph(double std20Gph) {
		this.stdGph = std20Gph;
	}
	public double getAbove20Ktas() {
		return above20Ktas;
	}
	public void setAbove20Ktas(double above20Ktas) {
		this.above20Ktas = above20Ktas;
	}
	public double getAbove20Gph() {
		return above20Gph;
	}
	public void setAbove20Gph(double above20Gph) {
		this.above20Gph = above20Gph;
	}

	
	
}
