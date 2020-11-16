package com.bitnei.apitest.testcases;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.bitnei.apitest.dataprovider.DataProviderMethod;
import com.bitnei.apitest.pro.Vehicles;
import com.bitnei.apitest.tool.DiffMethod;
import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONObject;

public class getVehCountPerMonitorPlatform {
	
	String url;
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	GetCookie getCookie = new GetCookie();
	String cookie = "";
	
	@Parameters({"host"})
	@BeforeClass
	public void setUp(String host) {
		url = host + "/rest/monitor/monitor/vehCountPerMonitorPlatform";
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
	
	@Test(description="获取实时车辆信息",priority =1,dataProvider="dataprovider2",
			dataProviderClass=DataProviderMethod.class)
	public void getVehCountPerMonitorPlatformStatus(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/x-www-form-urlencoded"); //这个在postman中可以查询到
		headermap.put("Cookie",cookie );	
		//入参设置
//		Vehicles vehiches = new Vehicles(10,"","",1,"","LS4AAB3C0GG700008");
//		String vehichesJsonString = JSON.toJSONString(vehiches);
//		System.out.println("vehichesJsonString------------"+vehichesJsonString);
		//调用接口
		//closeableHttpResponse = restClient.post(url, vehichesJsonString, headermap);
		closeableHttpResponse = restClient.get(url, headermap);
		//System.out.println("closeableHttpResponse------------"+closeableHttpResponse);
		
		HttpEntity entity = closeableHttpResponse.getEntity();
		String str = EntityUtils.toString(entity, "utf-8");
		System.out.println("source==========="+str);
		JSONObject lastobject = new JSONObject();
		System.out.println("except =============="+st);
		lastobject = diffMethod.diffFormatJson(JSONObject.fromObject(str),JSONObject.fromObject(st));
		System.out.println(lastobject.toString());
		JSONObject jsonDiff = new JSONObject();
		Assert.assertEquals(lastobject.toString(), "{}");
	}

}
