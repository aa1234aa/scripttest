package com.bitnei.apitest.testcases.openapi;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONObject;

public class OauthToken {
	String url;
	String urlpara = "";
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	String authorization = "";
	
	@Parameters({"hostopenapi"})	
	public String GetOauthToken(String hostopenapi) throws ClientProtocolException, IOException {
		restClient = new RestClient();
		url = hostopenapi + "/auth/oauth/token";
		//准备请求头信息
		HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"); 		
		//入参设置            		
		urlpara= url+"?client_id=zhuhai5338803e1aed4e276be2b3ffbd703417"
				+ "&client_secret=zhuhai5338803e1aed4e276be2b3ffbd703417&grant_type=password"
				+ "&password=ABC123...&scope=read&tenant=zhuhai&username=admin";
		//调用接口
		closeableHttpResponse = restClient.post(urlpara, headermap);	
		HttpEntity entity = closeableHttpResponse.getEntity();
		String str = EntityUtils.toString(entity, "utf-8");
		JSONObject object=JSONObject.fromObject(str);
		String token = object.getString("access_token");
		return token;
	}
}
