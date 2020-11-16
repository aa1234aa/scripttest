package com.bitnei.apitest.testcases.saas;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.bitnei.apitest.tool.DiffMethod;
import com.bitnei.apitest.utils.ExcelReader;
import com.bitnei.apitest.utils.ExcleUtil;
import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONObject;

public class UserInfoQueryByExcel {
	String host;
	String url;
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	GetCookieSaaS getCookisaas = new GetCookieSaaS();
	String authorization = "";
	ExcelReader ex ;
    ExcleUtil excleUtil;
		
	@Parameters({"hostsaas","pathroad"})
	@BeforeClass
	public void setUp(String hostsaas,String pathroad) {
		host = hostsaas;	
		String ExcelFilePath= pathroad ;
        String sheetName="Sheet1";
		//设置cookie		
		try {
			 authorization = getCookisaas.login(hostsaas);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
        ex = new ExcelReader(ExcelFilePath, sheetName);
        try {
            excleUtil.setExcleFile( ExcelFilePath,sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	@Test(description="saas平台用户查询",priority =0,dataProvider="dp")
	public void UserInfoQueryData(String url,String paras,String result) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		headermap.put("Authorization","Bearer "+authorization);
		//入参设置
		url= host +url;
		//调用接口	
		closeableHttpResponse = restClient.post(url, paras, headermap);		
		HttpEntity entity = closeableHttpResponse.getEntity();
		String str = EntityUtils.toString(entity, "utf-8");
		JSONObject lastobject = new JSONObject();
		lastobject = diffMethod.diffFormatJson(JSONObject.fromObject(str),JSONObject.fromObject(result));
		Assert.assertEquals(lastobject.toString(), "{}");
	}
	
	@DataProvider
    public Object[][] dp(){
     Object[][] sheetData = ex.getSheetDataByPD();
        return  sheetData ;
    }

}
