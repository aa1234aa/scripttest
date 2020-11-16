package com.bitnei.apitest.pro;

public class Vehicles {
	
	private int limit;
	private String model;
	private String monitorPlatform;
	private int start;
	private String vehProperty;
	private String vin;
	
	
	public Vehicles(int limit, String model, String monitorPlatform, int start, String vehProperty, String vin) {
		super();
		this.limit = limit;
		this.model = model;
		this.monitorPlatform = monitorPlatform;
		this.start = start;
		this.vehProperty = vehProperty;
		this.vin = vin;
	}
	
	public Vehicles() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getMonitorPlatform() {
		return monitorPlatform;
	}
	public void setMonitorPlatform(String monitorPlatform) {
		this.monitorPlatform = monitorPlatform;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public String getVehProperty() {
		return vehProperty;
	}
	public void setVehProperty(String vehProperty) {
		this.vehProperty = vehProperty;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}


}
