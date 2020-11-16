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

public class RunningData {
	String url;
	String urlpara = "";
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	String authorization = "";
	OauthToken oauthtoken = new OauthToken();
	
	@Parameters({"hostopenapi"})
	@BeforeClass
	public void setUp(String hostopenapi) {
		url = hostopenapi + "/external/1/zhuhai/api/runningData";
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
	@Test(description="车辆行驶数据查询正常",priority =0,dataProvider="dataprovider10",
			dataProviderClass=ZhuHaiProvider.class)
	public void EnergyConservationNormal(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Authorization","Bearer "+authorization);
		//入参设置             
		urlpara= url+"?licensePlate=粤C00859D&from=2019-03-01+12:00:00&to=2019-03-01+13:30:00";
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

	@Test(description="车辆行驶数选择时间范围不得超过一个自然日",priority =0,dataProvider="dataprovider4",
			dataProviderClass=ZhuHaiProvider.class)
	public void EnergyConservationUpScope(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Authorization","Bearer "+authorization);
		//入参设置             
		urlpara= url+"?licensePlate=粤C00859D&from=2019-03-01+12:00:00&to=2019-03-03+13:30:00";
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
	
	@Test(description="车辆行驶数数据查询车牌号为空",priority =0,dataProvider="dataprovider12",
			dataProviderClass=ZhuHaiProvider.class)
	public void EnergyConservationNolicensePlate(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Authorization","Bearer "+authorization);
		//入参设置             
		urlpara= url+"?licensePlate=&from=2019-03-01+12:00:00&to=2019-03-03+13:30:00";
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
	
	@Test(description="车辆行驶数数据查询token无效",priority =0,dataProvider="dataprovider13",
			dataProviderClass=ZhuHaiProvider.class)
	public void EnergyConservationInvalidtoken(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Authorization","Bearer "+"f829155c-9374-4673-a005-10ce548af1");
		//入参设置             
		urlpara= url+"?licensePlate=&from=2019-03-01+12:00:00&to=2019-03-03+13:30:00";
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


}

