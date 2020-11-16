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
import com.bitnei.apitest.dataprovider.datasend.ForwardVehicleProvider;
import com.bitnei.apitest.pro.datasendpro.ForwardVehiclePro;
import com.bitnei.apitest.pro.datasendpro.TaskVehiclePro;
import com.bitnei.apitest.testcases.GetCookie;
import com.bitnei.apitest.tool.DiffMethod;
import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONObject;

public class ForwardVehicleAddVehicle {
	String url;
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	GetCookie getCookie = new GetCookie();
	String cookie = "";
	
//	@Resource(name="forwardvehicledao") 
//    private ForwardVehicleDao forwardvehicledao;
	
	@Parameters({"host"})
	@BeforeClass
	public void setUp(String host) {
		url = host + "/rest/dataForwardConfig/task/addVehicle";
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
	
	@Test(description="数据转发平台任务添加车辆",priority =0,dataProvider="dataprovider4",
			dataProviderClass=ForwardVehicleProvider.class)
	public void ForwardVehicleAddVehicles(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		headermap.put("Cookie",cookie );	
		//入参设置
		TaskVehiclePro taskvehiclepro = new TaskVehiclePro();
		taskvehiclepro.setPlatformId("92f9c3e5-30d0-11e9-9869-90e2bae77ed8");
		taskvehiclepro.setTaskId(23);
		taskvehiclepro.setVehicleIds("458763");
		
		String proJsonString = JSON.toJSONString(taskvehiclepro);
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
}
