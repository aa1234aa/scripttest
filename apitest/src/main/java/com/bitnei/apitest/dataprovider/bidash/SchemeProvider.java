package com.bitnei.apitest.dataprovider.bidash;

import org.testng.annotations.DataProvider;

public class SchemeProvider {
	
	@DataProvider(name="dataprovider1")  
    public static Object[][] dataProvider1(){  
        return new Object[][]{  
        	{"{\"status\":200,\"errorMsg\":null,\"data\":null,\"error\":false}"}
        };
    }
	
	@DataProvider(name="dataprovider2")  
    public static Object[][] dataProvider2(){  
        return new Object[][]{  
        	{"{\"status\":500,\"errorMsg\":\"该方案名称已经存在！！！\",\"data\":null,\"error\":true}"}
        };
    }

}
