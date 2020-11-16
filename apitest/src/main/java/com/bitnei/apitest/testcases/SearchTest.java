package com.bitnei.apitest.testcases;

import java.io.IOException;

import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.bitnei.apitest.pro.BischemePro;
import com.bitnei.apitest.pro.ResultBean;
import com.bitnei.apitest.tool.DiffMethod;

import net.sf.json.JSONObject;

public class SearchTest {
	
	
	
    //@Override
    @Transactional
   // @Log
    @Test
    public void delUser() {
        // TODO Auto-generated method stub
//        ResultBean resultBean = new ResultBean();
//        resultBean.setResult(false);
        
//        BischemePro bischemePro = new BischemePro();
//
//        BischemeDao bischemeDao = new BischemeDao();
//        int n = bischemeDao.addLocal(bischemePro);
//        System.out.println(n);

        
    }


//	     @Test()
//	     public void testcase1() throws IOException {
//		 DiffMethod diffMethod = new DiffMethod();
//
//	        String str1 = "{\"status\":201,\"msg\":\"今天您已经领取过，明天可以继续领取哦！\",\"res\":{\"remainCouponNum\":\"5\",\"userId\":\"123123213222\"}}";
//
//	        JSONObject jsonObject1 = JSONObject.fromObject(str1);
//
//	        String str2 = "{\"status\":201,\"msg2\":\"今天您已经领取过，明天可以继续领取哦！\",\"res\":{\"remainCouponNum\":\"5\",\"userId\":\"123123213222\"}}";
//
//	        JSONObject jsonObject2 = JSONObject.fromObject(str2);
//
//	        String str3 = "{\"status\":null,\"msg\":\"今天您已经领取过，明天可以继续领取哦！\",\"res\":{\"remainCouponNum\":\"5\",\"userId\":\"123123213222\"}}";
//
//	        JSONObject jsonObject3 = JSONObject.fromObject(str3);
//
//	        System.out.println("转换成JSONschame:" + diffMethod.generateJsonSchema(jsonObject1).toString());
//
//	        System.out.println("当前str2没有msg字段: " + diffMethod.diffFormatJson(jsonObject2,jsonObject1).toString());
//
//	        System.out.println("当前str2中的status为null值:" + diffMethod.diffFormatJson(jsonObject3,jsonObject1).toString());
//
//
//	    }


}
