package com.bitnei.apitest.pro.bidash;

import com.bitnei.apitest.annotation.pro.TableName;
import com.bitnei.apitest.pro.Po;

@TableName(name="bi_scheme")
public class SchemePro extends Po{
	private String id;
	private String schemeName;
	private int status;
	private String templateType;
	private String description;

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
		
}
