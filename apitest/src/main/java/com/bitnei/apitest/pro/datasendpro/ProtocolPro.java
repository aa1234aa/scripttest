package com.bitnei.apitest.pro.datasendpro;

import com.bitnei.apitest.annotation.pro.TableName;
import com.bitnei.apitest.pro.Po;


@TableName(name="forward_protocol")
public class ProtocolPro extends Po{
	
	private String id;
	private String name;
	private String notes;
	private String port;
	private int ruleNo;
	private int type;
	//private String create_time;
	
	
	
	public int getRuleNo() {
		return ruleNo;
	}
	public void setRuleNo(int ruleNo) {
		this.ruleNo = ruleNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
//	public String getCreate_time() {
//		return create_time;
//	}
//	public void setCreate_time(String create_time) {
//		this.create_time = create_time;
//	}

	
	
}
