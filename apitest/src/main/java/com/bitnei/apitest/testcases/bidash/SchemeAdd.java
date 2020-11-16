package com.bitnei.apitest.testcases.bidash;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.bitnei.apitest.dao.impl.ProtocolDao;
import com.bitnei.apitest.dao.impl.SchemeDao;
import com.bitnei.apitest.dataprovider.bidash.SchemeProvider;
import com.bitnei.apitest.dataprovider.datasend.ForwardVehicleProvider;
import com.bitnei.apitest.dataprovider.datasend.PlatFormProvider;
import com.bitnei.apitest.pro.Method;
import com.bitnei.apitest.pro.bidash.SchemePro;
import com.bitnei.apitest.pro.datasendpro.ProtocolPro;
import com.bitnei.apitest.sql.C;
import com.bitnei.apitest.testcases.GetCookie;
import com.bitnei.apitest.tool.DiffMethod;
import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONObject;

public class SchemeAdd {
	String url;
	String url1;
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	GetCookie getCookie = new GetCookie();
	String cookie = "";
	
//	@Resource(name="forwardvehicledao") 
//    private ForwardVehicleDao forwardvehicledao;
	
	@Parameters({"host7789"})
	@BeforeClass
	public void setUp(String host) {
		url = host + "/rest/biDash/scheme";
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
	
	
	@Test(description="配置后台新增方案",priority =0,dataProvider="dataprovider1",
			dataProviderClass=SchemeProvider.class)
	public void SchemeAddData(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		headermap.put("Cookie",cookie );	
		//入参设置
		SchemePro schemepro = new SchemePro();
		schemepro.setSchemeName("autotestschemename");
		schemepro.setTemplateType("0");
		schemepro.setStatus(0);
		
		String proJsonString = JSON.toJSONString(schemepro);
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
	
	@Test(description="配置后台新增方案名称重复",priority =1,dataProvider="dataprovider2",
			dataProviderClass=SchemeProvider.class)
	public void SchemeAddDataRep(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		headermap.put("Cookie",cookie );	
		//入参设置
		SchemePro schemepro = new SchemePro();
		schemepro.setSchemeName("autotestschemename");
		schemepro.setTemplateType("0");
		schemepro.setStatus(0);
		
		String proJsonString = JSON.toJSONString(schemepro);
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
	

	@Test(description="配置后台删除方案",priority =2,dataProvider="dataprovider1",
			dataProviderClass=PlatFormProvider.class)
	public void SchemeDeleteData(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-mybatis.xml");
		SchemeDao schemedao = (SchemeDao) context.getBean("schemedao");
		List<SchemePro> list = schemedao.list(Method.where("scheme_name", C.EQ, "autotestschemename"));
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		headermap.put("Cookie",cookie );	
		//入参设置	 

		//调用接口
		url1=url+"/"+list.get(0).getId();
		System.out.println("url1------------"+url1);	
		closeableHttpResponse = restClient.delete(url1, headermap);
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
