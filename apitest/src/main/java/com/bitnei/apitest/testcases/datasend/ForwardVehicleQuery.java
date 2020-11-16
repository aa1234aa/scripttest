package com.bitnei.apitest.testcases.datasend;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.bitnei.apitest.dao.impl.ForwardVehicleDao;
import com.bitnei.apitest.dao.impl.TaskDao;
import com.bitnei.apitest.dataprovider.datasend.ForwardVehicleProvider;
import com.bitnei.apitest.dataprovider.datasend.PlatFormProvider;
import com.bitnei.apitest.pro.datasendpro.ForwardVehiclePro;
import com.bitnei.apitest.pro.datasendpro.TaskPro;
import com.bitnei.apitest.testcases.GetCookie;
import com.bitnei.apitest.tool.DiffMethod;
import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONObject;

public class ForwardVehicleQuery {	
	String url;
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	GetCookie getCookie = new GetCookie();
	String cookie = "";
	
	@Resource(name="forwardvehicledao") 
    private ForwardVehicleDao forwardvehicledao;
	
	@Parameters({"host"})
	@BeforeClass
	public void setUp(String host) {
		url = host + "/rest/dataForwardConfig/forwardVehicle/selectForwardVehicle?start=1&limit=10";		             
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
	
	@Test(description="数据转发平台查询车辆byvin",priority =0,dataProvider="dataprovider1",
			dataProviderClass=ForwardVehicleProvider.class)
	public void ForwardVehicleByVin(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		headermap.put("Cookie",cookie );	
		//入参设置
		ForwardVehiclePro forwardvehiclepro = new ForwardVehiclePro();
		forwardvehiclepro.setVin("LS4ASE2A1HJ121001");
		forwardvehiclepro.setTenantName("chan_tenant_name");
		
		String proJsonString = JSON.toJSONString(forwardvehiclepro);
		System.out.println("proJsonString------------"+proJsonString);		
		//调用接口
		closeableHttpResponse = restClient.post(url, proJsonString, headermap);
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
	
	@Test(description="数据转发平台查询车辆by车牌号",priority =1,dataProvider="dataprovider1",
			dataProviderClass=ForwardVehicleProvider.class)
	public void ForwardVehicleBylicense(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json; charset=UTF-8"); //这个在postman中可以查询到
		headermap.put("Cookie",cookie );	
		//入参设置
		ForwardVehiclePro forwardvehiclepro = new ForwardVehiclePro();
		forwardvehiclepro.setLicensePlate("JHK979");//入参为汉字时，会乱码，暂没有解决
		forwardvehiclepro.setTenantName("chan_tenant_name");
		String proJsonString = JSON.toJSONString(forwardvehiclepro);
		System.out.println("proJsonString------------"+proJsonString);
		//调用接口
		closeableHttpResponse = restClient.post(url, proJsonString, headermap);
		System.out.println("closeableHttpResponse------------"+closeableHttpResponse);		
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
