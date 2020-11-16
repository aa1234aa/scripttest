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
import com.bitnei.apitest.dataprovider.EvDetailDataProvider;
import com.bitnei.apitest.pro.EvDetailPro;
import com.bitnei.apitest.tool.DiffMethod;
import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONObject;

public class EvDetail {
	String url;
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	GetCookie getCookie = new GetCookie();
	String cookie = "";
	
	@Parameters({"host"})
	@BeforeClass
	public void setUp(String host) {
		url = host + "/rest/queryapi/ev/detail/paged?pageNumber=1&pageSize=10";
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
	
	@Test(description="明细数据查询",priority =1,dataProvider="dataprovider1",
			dataProviderClass=EvDetailDataProvider.class)
	public void getVehCountPerMonitorPlatformStatus(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		headermap.put("Cookie",cookie );	
		//入参设置
		EvDetailPro evdetailpro = new EvDetailPro();
		evdetailpro.setVid("LS5A3AJC1HB005375");
		evdetailpro.setFieldIds("1,2,3,4,5,6,7,8,9,10,11,12,13");
		evdetailpro.setPeriods("{\"from\":1537596960000,\"to\":1537596970000}");
		//evdetailpro.setPeriods("321", "123");
		
		String evdetailproJsonString = JSON.toJSONString(evdetailpro);
		System.out.println("evdetailproJsonString------------"+evdetailproJsonString);
		//调用接口
		closeableHttpResponse = restClient.post(url, evdetailproJsonString, headermap);
		//closeableHttpResponse = restClient.post(url, headermap,evdetailpro);
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
