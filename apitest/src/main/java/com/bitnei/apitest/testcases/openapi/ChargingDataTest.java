package com.bitnei.apitest.testcases.openapi;

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

public class ChargingDataTest {
	String host;
	String url;
	String urlpara = "";
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	String authorization = "";
	OauthToken oauthtoken = new OauthToken();
	ExcelReader ex ;
    ExcleUtil excleUtil;
	
	@Parameters({"hostopenapi","pathroad"})
	@BeforeClass
	public void setUp(String hostopenapi,String pathroad)  {
		host = hostopenapi;	
		String ExcelFilePath= pathroad ;
        String sheetName="Sheet2";
		try {
			authorization = oauthtoken.GetOauthToken(hostopenapi);
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
	
	@Test(description="查询珠海车辆充电数据",priority =0,dataProvider="dp")
	public void ChargingDataNormal(String desc,String url,String paras,String result) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		DiffMethod diffMethod = new DiffMethod();
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); 
		headermap.put("Authorization","Bearer "+authorization);
		//入参设置             
		urlpara= host+url+paras;
		//调用接口
		closeableHttpResponse = restClient.get(urlpara, headermap);	
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
