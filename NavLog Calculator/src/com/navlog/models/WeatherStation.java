package com.navlog.models;

import java.util.List;

public class WeatherStation 
{
	private String ICAO;
	private String atisCode;
	private String observationTime;
	private double elevation;
	private double visibility;
	private double windSpeed;
	private double windDirection;
	private double temperature;
	private List<String> skyConditions;
	public String getICAO() {
		return ICAO;
	}
	public void setICAO(String iCAO) {
		ICAO = iCAO;
	}
	public String getAtisCode() {
		return atisCode;
	}
	public void setAtisCode(String atisCode) {
		this.atisCode = atisCode;
	}
	public double getElevation() {
		return elevation;
	}
	public void setElevation(double elevation) {
		this.elevation = elevation;
	}
	public String getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(String observationTime) {
		this.observationTime = observationTime;
	}
	public double getVisibility() {
		return visibility;
	}
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public double getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(double windDirection) {
		this.windDirection = windDirection;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public List<String> getSkyConditions() {
		return skyConditions;
	}
	public void setSkyConditions(List<String> skyConditions) {
		this.skyConditions = skyConditions;
	}
			
}
