package com.bitnei.apitest.pro.datasendpro;

import com.bitnei.apitest.annotation.pro.TableName;
import com.bitnei.apitest.pro.Po;

@TableName(name="forward_task_vehicle")
public class ForwardVehiclePro extends Po{
	private String vin;
	private String licensePlate;
	private String monitorPlatform;
	private String platformId;
	private String protocolId;
	private String vendor;
	private String tenantName;

	
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public String getMonitorPlatform() {
		return monitorPlatform;
	}
	public void setMonitorPlatform(String monitorPlatform) {
		this.monitorPlatform = monitorPlatform;
	}
	public String getPlatformId() {
		return platformId;
	}
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	public String getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	
	
}
