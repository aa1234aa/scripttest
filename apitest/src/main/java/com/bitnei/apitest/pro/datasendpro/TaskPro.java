package com.bitnei.apitest.pro.datasendpro;

import com.bitnei.apitest.annotation.pro.TableName;
import com.bitnei.apitest.annotation.pro.TempField;
import com.bitnei.apitest.pro.Po;

@TableName(name="forward_task")
public class TaskPro extends Po{
	
	private int id;
	private String name;
	private String platformId;
	private String protocolId;
	private int isEncrypt;
	private String tenantName;
	
	

	public TaskPro() {
		super();
	}
	public TaskPro(String name, String platformId, String protocolId, int isEncrypt, String tenantName) {
		super();
		this.name = name;
		this.platformId = platformId;
		this.protocolId = protocolId;
		this.isEncrypt = isEncrypt;
		this.tenantName = tenantName;
	}
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getIsEncrypt() {
		return isEncrypt;
	}
	public void setIsEncrypt(int isEncrypt) {
		this.isEncrypt = isEncrypt;
	}

}
