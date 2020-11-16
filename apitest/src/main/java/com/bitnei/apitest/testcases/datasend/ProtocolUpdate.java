package com.bitnei.apitest.testcases.datasend;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.bitnei.apitest.dao.impl.PlatFormDao;
import com.bitnei.apitest.dao.impl.ProtocolDao;
import com.bitnei.apitest.dataprovider.datasend.PlatFormProvider;
import com.bitnei.apitest.pro.Method;
import com.bitnei.apitest.pro.datasendpro.PlatFormPro;
import com.bitnei.apitest.pro.datasendpro.ProtocolPro;
import com.bitnei.apitest.sql.C;
import com.bitnei.apitest.testcases.GetCookie;
import com.bitnei.apitest.tool.DiffMethod;
import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONObject;

public class ProtocolUpdate {
	String url;
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	GetCookie getCookie = new GetCookie();
	String cookie = "";
	
	@Resource(name="protocoldao") 
    private ProtocolDao protocoldao;
	
	@Parameters({"host"})
	@BeforeClass
	public void setUp(String host) {
		url = host + "/rest/dataForwardConfig/protocol/update";
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
	
	@Test(description="数据转发平台新增协议",priority =0,dataProvider="dataprovider1",
			dataProviderClass=PlatFormProvider.class)
	public void ProtocolUpdateData(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-mybatis.xml");
		ProtocolDao protocoldao = (ProtocolDao) context.getBean("protocoldao");
		List<ProtocolPro> list = protocoldao.list(Method.where("name", C.EQ, "autotestng"));
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		headermap.put("Cookie",cookie );	
		//入参设置
		list.get(0).setPort("8090");
		list.get(0).setNotes("update");
		String proJsonString = JSON.toJSONString(list.get(0));
		System.out.println("proJsonString------------"+proJsonString);
		//调用接口
		closeableHttpResponse = restClient.post(url, proJsonString, headermap);		
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
