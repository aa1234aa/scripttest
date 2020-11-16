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

import com.bitnei.apitest.dao.impl.ProtocolDao;
import com.bitnei.apitest.dao.impl.TaskDao;
import com.bitnei.apitest.dataprovider.datasend.PlatFormProvider;
import com.bitnei.apitest.pro.Method;
import com.bitnei.apitest.pro.datasendpro.ProtocolPro;
import com.bitnei.apitest.pro.datasendpro.TaskPro;
import com.bitnei.apitest.sql.C;
import com.bitnei.apitest.testcases.GetCookie;
import com.bitnei.apitest.tool.DiffMethod;
import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONObject;

public class TaskDelete {
	String url;
	String url1;
	String url2;
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	GetCookie getCookie = new GetCookie();
	String cookie = "";
	
	@Resource(name="taskdao") 
    private TaskDao taskdao;
	
	@Parameters({"host"})
	@BeforeClass
	public void setUp(String host) {
		url1 = host + "/rest/dataForwardConfig/task/";
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
	
	@Test(description="数据转发平台配置删除任务",priority =3,dataProvider="dataprovider1",
			dataProviderClass=PlatFormProvider.class)
	public void TaskDeleteData(String st) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-mybatis.xml");
		TaskDao taskdao = (TaskDao)context.getBean("taskdao");
		List<TaskPro> list = taskdao.list(Method.where("name", C.EQ, "autotesttask"));
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		headermap.put("Cookie",cookie );	
		//入参设置	 

		//调用接口
		url=url1+list.get(0).getId();
		url2= url+"/"+list.get(0).getPlatformId();
		System.out.println("url2------------"+url2);	
		closeableHttpResponse = restClient.delete(url2, headermap);
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
