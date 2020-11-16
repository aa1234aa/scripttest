package com.bitnei.apitest.pro;

import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;

import net.sf.json.JSONObject;

public class EvDetailPro {
	private String vid;
	private List<String> fieldIds;
	private JSONObject periods;	
	
	
	public EvDetailPro() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}



	public List<String> getFieldIds() {
		return fieldIds;
	}



	public void setFieldIds(String fieldIds) {
        List<String> lis = Arrays.asList(fieldIds.split(","));
		this.fieldIds = lis;
	}



	public JSONObject getPeriods() {
		return periods;
	}



	public void setPeriods(String periods) {
		if(periods != "") {
		JSONObject jsonperiods = JSONObject.fromObject(periods);  
		this.periods = jsonperiods;
		}
	}

//	public List<Integer> getFieldIds() {
//		return fieldIds;
//	}
//	public void setFieldIds(int n) {
//		List<Integer> fieldIds = new ArrayList<Integer>();
//		fieldIds.add(n);
//		System.out.println(fieldIds);
//		this.fieldIds = fieldIds;
//	}
	
		
//	public Object getPeriods() {
//		return periods;
//	}
//	public void setPeriods(String from, String to) {
//		JsonObject periods = new JsonObject();
//		JsonParser jsonParser = new JsonParser();
//		periods.add("from",jsonParser.parse(from) );
//		periods.add("to", jsonParser.parse(to));
//		System.out.println(periods);
//		this.periods=periods;
//	}
	
	
}
