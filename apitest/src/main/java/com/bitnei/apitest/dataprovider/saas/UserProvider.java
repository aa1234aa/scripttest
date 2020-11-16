package com.bitnei.apitest.dataprovider.saas;

import org.testng.annotations.DataProvider;

public class UserProvider {
	@DataProvider(name="dataprovider1")  
    public static Object[][] dataProvider1(){  
        return new Object[][]{  
        	{"{\"status\":200,\"errorMsg\":null,\"data\":[{\"id\":\"5a51de0cc97e48ea8580d46796724a82\",\"username\":\"xiejunjun@bitnei.cn\",\"nickName\":\"xiejunjun\",\"email\":\"xiejunjun@bitnei.cn\",\"mobile\":\"13122223333\",\"password\":null,\"title\":\"\",\"status\":\"ACTIVE\",\"createBy\":\"yaomeng@bitnei.cn\",\"createTime\":\"2019-02-22 06:25:52\",\"lastUpdateTime\":\"2019-03-08 10:29:02\",\"tenant\":\"bitnei-cn\",\"origin\":\"INVITE\",\"admin\":false}],\"error\":false}"}
        };
    }
}
