package com.bitnei.apitest.testcases.saas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.bitnei.apitest.pro.datasendpro.PlatFormPro;
import com.bitnei.apitest.pro.saas.UserPro;
import com.bitnei.apitest.utils.RestClient;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class GetCookieSaaS {
	

	@Parameters({"hostsaas"})
	public String login(String hostsaas) throws ClientProtocolException, IOException {
		//用户名密码登录
        String login = hostsaas + "/user/login?username=yuanpeipei@bitnei.cn&password=123abc";  
        RestClient  restClient = new RestClient();
        CloseableHttpResponse closeableHttpResponse = restClient.post(login);
        //System.out.println("login =============="+login);
        HttpEntity entity = closeableHttpResponse.getEntity();
		String str = EntityUtils.toString(entity, "utf-8");		
		JSONObject jsonobject = JSONObject.fromObject(str);
		//System.out.println("jsonobject =============="+jsonobject);
		String access_token = jsonobject.getJSONObject("data").getJSONObject("token").getString("access_token");	
		//System.out.println("data =============="+access_token);
		
		return access_token;		
	}
	//不需要cookie
//	@Test
//	@Parameters({"hostsaas"})
//	public String statistics(String hostsaas) throws IOException  {
//		String cookie = "";
//		String statistics = hostsaas + "/userInfo/statistics";	
//		BasicCookieStore cookieStore = new BasicCookieStore();
//		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
//		HttpGet httpget = new HttpGet(statistics);
//		String authorization = "Bearer "+ login(hostsaas);
//		httpget.addHeader("Authorization", authorization);
//		CloseableHttpResponse response1 = httpclient.execute(httpget);	
//		try {
//				HttpEntity entity = response1.getEntity();
//				BufferedReader rd = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
//				System.out.println("--------------------Next will show something else");
//				String line = "";
//				while ((line = rd.readLine()) != null) {
//				System.out.println(line);
//				}
//				System.out.println("entity " + entity);
//				System.out.println("Login form get: " + response1.getStatusLine());
//				EntityUtils.consume(entity);
//				System.out.println("Initial set of cookies:");
//				List<org.apache.http.cookie.Cookie> cookies = cookieStore.getCookies();
//				if (cookies.isEmpty()) {
//					System.out.println("None");
//					} else {
//					for (int i = 0; i < cookies.size(); i++) {
//					cookie= cookies.get(0).getValue();
//					System.out.println("cookie======"+cookie);
//
//					}
//					}
//			}
//				finally {
//					response1.close();
//				}
//		return cookie;
//	}
}