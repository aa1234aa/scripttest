package com.bitnei.apitest.dataprovider;

import org.testng.annotations.DataProvider;

public class EvDetailDataProvider {
	//
	@DataProvider  
    public static Object[][] NoNameMethod(){  
        return new Object[][]{  
                {"DataWithNoName1"},  
                {"DataWithNoName2"},  
                {""}  
        };  
    }  
    //{"vid":"LS5A3AJC1HB005375","periods":{"from":1537560568000,"to":1537600832000},"fieldIds":[1,2,3,4,5,6,7,8,9,10,11,12,13]}
    @DataProvider(name="dataprovider1")  
    public static Object[][] dataProvider1(){  
        return new Object[][]{  
        	{"{\"status\":200,\"errorMsg\":null,\"data\":{\"data\":[{\"terminalTime\":\"2018-09-22 14:16:10\",\"carStatus\":\"2\",\"chargeStatus\":\"0\",\"carMode\":\"1\",\"speed\":\"0.0\",\"totalOdometer\":\"32218.199219\",\"totalVoltage\":\"0.0\",\"totalCurrent\":\"0.0\",\"soc\":\"0\",\"dcStatus\":\"2\",\"insulateResistance\":\"0\"},{\"terminaltime\":\"2018-09-22 14:16:00\",\"carStatus\":\"2\",\"chargeStatus\":\"0\",\"carMode\":\"1\",\"speed\":\"0.0\",\"totalOdometer\":\"32218.199219\",\"totalVoltage\":\"0.0\",\"totalCurrent\":\"0.0\",\"soc\":\"0\",\"dcStatus\":\"2\",\"insulateResistance\":\"0\"}],\"titles\":[\"数据采集时间\",\"车辆状态\",\"充电状态\",\"运行模式\",\"车速(km/h)\",\"里程(km)\",\"总电压(V)\",\"总电流(A)\",\"SOC\",\"DC-DC状态\",\"绝缘电阻(kΩ)\"],\"page\":{\"pageSize\":10,\"pageNumber\":1,\"endIndex\":2,\"startIndex\":0,\"totalSize\":2,\"totalPage\":1}},\"error\":false}"}
        };
    }  

}
