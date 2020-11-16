package com.bitnei.apitest.dataprovider.datasend;

import org.testng.annotations.DataProvider;

public class PlatFormProvider {
	//
    @DataProvider(name="dataprovider1")  
    public static Object[][] dataProvider1(){  
        return new Object[][]{  
        	{"{\"status\":200,\"errorMsg\":null,\"data\":null,\"error\":false}"}
        };
    }
    
    @DataProvider(name="dataprovider2")  
    public static Object[][] dataProvider2(){  
        return new Object[][]{  
        	{"{\"status\":200,\"errorMsg\":null,\"data\":{\"total\":1,\"list\":[{\"id\":\"05486f02-188d-11e9-9869-90e2bae77ed8\",\"unitName\":\"yltest001\",\"forwardMode\":\"SOCKET\",\"address\":\"10.100.10.11\",\"username\":\"ul213456\",\"password\":\"ul213456\",\"nsPort\":\"205\",\"cdKey\":null,\"notes\":null,\"priority\":\"1\",\"staticForwardPlatform\":\"无\",\"isUse\":0,\"createTime\":1547532957000}],\"pageNum\":1,\"pageSize\":10,\"size\":1,\"startRow\":1,\"endRow\":1,\"pages\":1,\"prePage\":0,\"nextPage\":0,\"isFirstPage\":true,\"isLastPage\":true,\"hasPreviousPage\":false,\"hasNextPage\":false,\"navigatePages\":8,\"navigatepageNums\":[1],\"navigateFirstPage\":1,\"navigateLastPage\":1,\"firstPage\":1,\"lastPage\":1},\"error\":false}"}
        };
    }
    
    @DataProvider(name="dataprovider3")  
    public static Object[][] dataProvider3(){  
        return new Object[][]{  
        	{"{\"status\":500,\"errorMsg\":\"该平台已分配，不能重复添加\",\"data\":null,\"error\":true}"}
        };
    }
    
    @DataProvider(name="dataprovider4")  
    public static Object[][] dataProvider4(){  
        return new Object[][]{  
        	{"{\"status\":500,\"errorMsg\":\"任务名称已存在\",\"data\":null,\"error\":true}"}
        };
    }
    
}
