package com.bitnei.apitest.testcases.datasend;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.bitnei.apitest.dataprovider.datasend.PlatFormProvider;
import com.bitnei.apitest.pro.Method;
import com.bitnei.apitest.pro.datasendpro.PlatFormPro;
import com.bitnei.apitest.sql.C;
import com.bitnei.apitest.testcases.GetCookie;
import com.bitnei.apitest.tool.DiffMethod;
import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONObject;

public class PlatFormDel {
	String url;
	String url1;
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	GetCookie getCookie = new GetCookie();
	String cookie = "";
	PlatFormPro platformpro = new PlatFormPro();
	
	@Resource(name="platformdao") 
    private PlatFormDao platformdao;
	
	@Parameters({"host"})
	@BeforeClass
	public void setUp(String host) {
		url1 = host + "/rest/dataForwardConfig/platform/";
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
	
	@Test(description="数据转发平台配置删除数据",priority =1,dataProvider="dataprovider1",
			dataProviderClass=PlatFormProvider.class)
	public void PlatFormDelData(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-mybatis.xml");
		PlatFormDao platformdao = (PlatFormDao) context.getBean("platformdao");
		System.out.println("platformdao------------"+platformdao);	
		long t = System.currentTimeMillis();
		Long dateLong = Long.valueOf(t);
		//Date d = new Date(dateLong);
		String d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
		System.out.println("d------------"+d);	
		
//		platformdao.addLocal(platformpro);
		List<PlatFormPro> list = platformdao.list(Method.where("unit_name", C.EQ, "autotestmodif"));
		//System.out.println("list------------"+list);
		//System.out.println("list0------------"+list.get(0).getUnitName());
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		headermap.put("Cookie",cookie );	
		//入参设置	 
//		 list.get(0).setUnitName("autotestmodif");
//		 list.get(0).setCreateTime(t);	
//		 list.get(0).getId();
//		String proJsonString = JSON.toJSONString(list.get(0));
//		System.out.println("proJsonString------------"+proJsonString);
		//调用接口
		url=url1+list.get(0).getId();
		closeableHttpResponse = restClient.delete(url, headermap);
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
		//判断修改名称后，数据库中可以查到
//		boolean b1 = platformdao.isExist(Method.where("unit_name", C.EQ, "autotestmodif"));
//		Assert.assertEquals(b1, false);
	}

}
