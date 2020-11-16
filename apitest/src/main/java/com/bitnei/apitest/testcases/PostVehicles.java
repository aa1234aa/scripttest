package com.bitnei.apitest.testcases;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.bitnei.apitest.dataprovider.DataProviderMethod;
import com.bitnei.apitest.pro.Vehicles;
import com.bitnei.apitest.tool.DiffMethod;
import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONObject;

public class PostVehicles {
	RestClient restClient = new RestClient();
	DiffMethod diffMethod = new DiffMethod();
	String url;
	
	CloseableHttpResponse closeableHttpResponse;
	GetCookie getCookie = new GetCookie();
	String cookie = "";
	
	
	
	@BeforeMethod
	@Parameters({"host"})
	public void setUp(String host) {
		url = host +"/rest/monitor/sysVehicle/vehicles/";
		System.out.println(host);
		//设置cookie		
		try {
			 cookie = getCookie.login();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Test(description="获取车辆列表,判断返回200",priority =0,dataProvider="dataprovider1",dataProviderClass=DataProviderMethod.class)
	public void postVehiclesForStatus(String st) throws ClientProtocolException, IOException {
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		headermap.put("Cookie", cookie);
		System.out.println("Cookie-------"+cookie);
		//入参设置
		Vehicles vehiches = new Vehicles(10,"","",1,"","LS4AAB3C0GG700008");
		String vehichesJsonString = JSON.toJSONString(vehiches);
		System.out.println("vehichesJsonString------------"+vehichesJsonString);	
		System.out.println("url------------"+url);	
	    closeableHttpResponse = restClient.post(url, vehichesJsonString, headermap);
		//System.out.println("closeableHttpResponse------------"+closeableHttpResponse);
		//验证状态码是不是200
		int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();		
		HttpEntity entity = closeableHttpResponse.getEntity();
		String str = EntityUtils.toString(entity, "utf-8");
		System.out.println("source==========="+str);
		Assert.assertEquals(statusCode, 200,"status code is not 200");
	}

//	@Test(description="获取车辆列表通VIN查询",priority =1,dataProvider="dataprovider1",dataProviderClass=DataProviderMethod.class)
//	public void PostVehiclesByVin(String st) throws ClientProtocolException, IOException {
//
//		//准备请求头信息
//		HashMap<String,String> headermap = new HashMap<String,String>();
//		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
//		headermap.put("Cookie", cookie);	
//		//入参设置
//		Vehicles vehiches = new Vehicles(10,"","",1,"","LS4AAB3C0GG700008");
//		String vehichesJsonString = JSON.toJSONString(vehiches);
//		System.out.println("vehichesJsonString------------"+vehichesJsonString);		
//		closeableHttpResponse = restClient.post(url, vehichesJsonString, headermap);		
//		//System.out.println("closeableHttpResponse------------"+closeableHttpResponse);		
//		HttpEntity entity = closeableHttpResponse.getEntity();
//		String str = EntityUtils.toString(entity, "utf-8");
//		System.out.println("source==========="+str);
//		JSONObject lastobject = new JSONObject();
//		System.out.println("except =============="+st);
//		lastobject = diffMethod.diffFormatJson(JSONObject.fromObject(str),JSONObject.fromObject(st));
//		System.out.println(lastobject.toString());
//		JSONObject jsonDiff = new JSONObject();
//		Assert.assertEquals(lastobject.toString(), "{}");
//	}
	
//	@Test(description="获取车辆列表通过平台查询",priority =2,dataProvider="dataprovider3",dataProviderClass=DataProviderMethod.class)
//	public void PostVehiclesByPlatform(String st) throws ClientProtocolException, IOException {
//
//		//准备请求头信息
//		HashMap<String,String> headermap = new HashMap<String,String>();
//		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
//		headermap.put("Cookie", cookie);	
//		//入参设置
//		Vehicles vehiches = new Vehicles(10,"","2",1,"","");
//		String vehichesJsonString = JSON.toJSONString(vehiches);
//		System.out.println("vehichesJsonString------------"+vehichesJsonString);		
//		closeableHttpResponse = restClient.post(url, vehichesJsonString, headermap);		
//		//System.out.println("closeableHttpResponse------------"+closeableHttpResponse);		
//		HttpEntity entity = closeableHttpResponse.getEntity();
//		String str = EntityUtils.toString(entity, "utf-8");
//		System.out.println("source==========="+str);
//		JSONObject lastobject = new JSONObject();
//		System.out.println("except =============="+st);
//		lastobject = diffMethod.diffFormatJson(JSONObject.fromObject(str),JSONObject.fromObject(st));
//		System.out.println(lastobject.toString());
//		JSONObject jsonDiff = new JSONObject();
//		Assert.assertEquals(lastobject.toString(), "{}");
//	}
	
	
}