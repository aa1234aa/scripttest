package com.bitnei.apitest.testcases.openapi;

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

import com.bitnei.apitest.dataprovider.openapi.ZhuHaiProvider;
import com.bitnei.apitest.tool.DiffMethod;
import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONObject;

public class ChargingData {
	String url;
	String urlpara = "";
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	String authorization = "";
	OauthToken oauthtoken = new OauthToken();
	
	@Parameters({"hostopenapi"})
	@BeforeClass
	public void setUp(String hostopenapi)  {
		url = hostopenapi + "/external/1/zhuhai/api/chargingData";	
		try {
			authorization = oauthtoken.GetOauthToken(hostopenapi);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test(description="查询珠海车辆充电数据正常",priority =0,dataProvider="dataprovider1",
			dataProviderClass=ZhuHaiProvider.class)
	public void ChargingDataNormal(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); 
		headermap.put("Authorization","Bearer "+authorization);
		//入参设置             
		urlpara= url+"?licensePlate=粤C00859D&from=2019-03-01+07:49:21&to=2019-03-01+10:49:21";
		//调用接口
		System.out.println("url------------"+url);	
		System.out.println("headermap------------"+headermap);
		closeableHttpResponse = restClient.get(urlpara, headermap);
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

	@Test(description="查询珠海车辆充电数据缺少from参数",priority =0,dataProvider="dataprovider2",
			dataProviderClass=ZhuHaiProvider.class)
	public void ChargingDataNoFrom(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); 
		headermap.put("Authorization","Bearer "+authorization);
		//入参设置             
		urlpara= url+"?licensePlate=粤C00859D&from=&to=2019-03-01+10:49:21";
		//调用接口
		closeableHttpResponse = restClient.get(urlpara, headermap);		
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
	
	@Test(description="查询珠海车辆充电数据缺少to参数",priority =0,dataProvider="dataprovider3",
			dataProviderClass=ZhuHaiProvider.class)
	public void ChargingDataNoTo(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); 
		headermap.put("Authorization","Bearer "+authorization);
		//入参设置             
		urlpara= url+"?licensePlate=粤C00859D&from=2019-03-01+10:49:21&to=";
		//调用接口
		closeableHttpResponse = restClient.get(urlpara, headermap);		
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
	
	@Test(description="查询珠海车辆充电数据选择时间范围不得超过一个自然日",priority =0,dataProvider="dataprovider4",
			dataProviderClass=ZhuHaiProvider.class)
	public void ChargingDataUpOneDay(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Authorization","Bearer "+authorization);
		//入参设置             
		urlpara= url+"?licensePlate=粤C00859D&from=2019-03-01+10:49:21&to=2019-03-03+10:49:21";
		//调用接口
		closeableHttpResponse = restClient.get(urlpara, headermap);		
		HttpEntity entity = closeableHttpResponse.getEntity();
		String str = EntityUtils.toString(entity, "utf-8");
		System.out.println("source==========="+str);
		JSONObject lastobject = new JSONObject();
		System.out.println("except =============="+st);
		lastobject = diffMethod.diffFormatJson(JSONObject.fromObject(str),JSONObject.fromObject(st));
		JSONObject jsonDiff = new JSONObject();
		Assert.assertEquals(lastobject.toString(), "{}");
	}
	
	@Test(description="查询珠海车辆充电数据选择时间范围开始时间不能大于结束时间",priority =0,dataProvider="dataprovider5",
			dataProviderClass=ZhuHaiProvider.class)
	public void ChargingDataWrongTime(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Authorization","Bearer "+authorization);
		//入参设置             
		urlpara= url+"?licensePlate=粤C00859D&from=2019-03-03+10:49:21&to=2019-03-02+10:49:21";
		//调用接口
		closeableHttpResponse = restClient.get(urlpara, headermap);		
		HttpEntity entity = closeableHttpResponse.getEntity();
		String str = EntityUtils.toString(entity, "utf-8");
		System.out.println("source==========="+str);
		JSONObject lastobject = new JSONObject();
		System.out.println("except =============="+st);
		lastobject = diffMethod.diffFormatJson(JSONObject.fromObject(str),JSONObject.fromObject(st));
		JSONObject jsonDiff = new JSONObject();
		Assert.assertEquals(lastobject.toString(), "{}");
	}
	
	@Test(description="查询珠海车辆充电数据缺少车牌号参数",priority =0,dataProvider="dataprovider14",
			dataProviderClass=ZhuHaiProvider.class)
	public void ChargingDataNo(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Authorization","Bearer "+authorization);
		//入参设置             
		urlpara= url+"?from=2019-03-03+10:49:21&to=2019-03-02+10:49:21";
		//调用接口
		closeableHttpResponse = restClient.get(urlpara, headermap);		
		HttpEntity entity = closeableHttpResponse.getEntity();
		String str = EntityUtils.toString(entity, "utf-8");
		System.out.println("source==========="+str);
		JSONObject lastobject = new JSONObject();
		System.out.println("except =============="+st);
		lastobject = diffMethod.diffFormatJson(JSONObject.fromObject(str),JSONObject.fromObject(st));
		JSONObject jsonDiff = new JSONObject();
		Assert.assertEquals(lastobject.toString(), "{}");
	}
	
}
