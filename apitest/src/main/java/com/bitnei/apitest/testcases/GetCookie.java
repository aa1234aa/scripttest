package com.bitnei.apitest.testcases;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.bitnei.apitest.utils.RestClient;


public class GetCookie {
	
	@Test
	public String login() throws ClientProtocolException, IOException {
		//获取浏览器首次返回cookie的url
        String loginUrl = "http://bdp-app.bitnei.cn:7788/rest/ras/dashboard";  
        //登录成功后，在域名下携带cookie,验证通过的url
		String urladmin = "http://bdp-app.bitnei.cn:7788/rest/ras/dashboard?token=admin";		
		//代理登录url,需要用户名密码
		String urls =   "http://10.10.26.2:19010/monitor2/inner_api/user/login?redirectUrl=http://bdp-app.bitnei.cn/dashboard/info";
        //登录后302跳转返回url为http://bdp-app.bitnei.cn/dashboard/info?token=admin,但这个url返回404,不可用于验证。坑啊！
		String cookie = "";
        HttpClient httpClient = new HttpClient();
        // 模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
        PostMethod postMethod = new PostMethod(urls);
        GetMethod getMethod = new GetMethod(loginUrl);
        GetMethod getMethod2 = new GetMethod(urladmin);
        
        // 设置登陆时要求的信息，用户名和密码
        NameValuePair[] data = { new NameValuePair("username", "admin"), new NameValuePair("password", "123") };
        postMethod.setRequestBody(data);

        try {
            // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            int statusCode=httpClient.executeMethod(getMethod);
            //System.out.println("statusCode = "+statusCode);              
            // 获得登陆后的 Cookie
            Cookie[] cookies = httpClient.getState().getCookies();
            StringBuffer tmpcookies = new StringBuffer();
            for (Cookie c : cookies) {
                tmpcookies.append(c.toString() + ";");
                cookie =c.toString();
                System.out.println("cookies = "+cookie);
            }           
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        HashMap<String,String> headermap = new HashMap<String,String>();
		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		headermap.put("Cookie", cookie);
		//System.out.println(headermap);
				
		RestClient restClient = new RestClient();
		CloseableHttpResponse closeableHttpResponse;
		closeableHttpResponse = restClient.get(urladmin, headermap);
		int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
		//System.out.println(statusCode);
		//System.out.println(closeableHttpResponse);
        
		return cookie;
		
	}

}
