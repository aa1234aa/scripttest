package com.bitnei.apitest.dataprovider;

import org.testng.annotations.DataProvider;

public class DataProviderMethod {  
	    //无指定数据名称，默认使用方法名  
	    @DataProvider  
	    public static Object[][] NoNameMethod(){  
	        return new Object[][]{  
	                {"DataWithNoName1"},  
	                {"DataWithNoName2"},  
	                {""}  
	        };  
	    }  
	    //查询vin=LS4AAB3C0GG700008的车辆列表
	    @DataProvider(name="dataprovider1")  
	    public static Object[][] dataProvider1(){  
	        return new Object[][]{  
	                {"{\"status\":200,\"errorMsg\":null,\"data\":{\"total\":2,\"list\":[{\"id\":\"21\",\"vin\":\"LS4AAB3C0GG700008\"}]},\"error\":true}"}  
	        };  
	    }  
	    //获取时实车辆信息通过vinLS4AAB3C0GG700008查询
	    @DataProvider(name="dataprovider2")  
	    public static Object[][] dataProvider2(){  
	        return new Object[][]{  
	                {"{\"status\":200,"
	                		+ "\"errorMsg\":null,"
	                		+ "\"data\":[{\"id\":1,"
	                		+ "\"monitorPlatform\":\"智云平台\","
	                		+ "\"num\":274419},{\"id\":2,"
	                		+ "\"monitorPlatform\":\"英泰斯特\","
	                		+ "\"num\":14}],"
	                		+ "\"error\":false}"} 
	        };  
	    }  
	    //查询条件为平台
	    @DataProvider(name="dataprovider3")  
	    public static Object[][] dataProvider3(){  
	        return new Object[][]{  
	                {"{\"status\":200,\"errorMsg\":null,\"data\":{\"total\":14,\"list\":[{\"id\":\"458748\",\"vin\":\"LS4ASE2E7JJ227011\",\"licensePlate\":\"新QLR005\",\"vehSeriesNo\":\"S301-18\",\"vehModelNo\":\"SC6469KBAH5\",\"vehModelName\":\"JL476ZQCF 1.5 国五6AT 6.5 STT\",\"vehConfNo\":\"SC6469KBAH5.S3\",\"vehProperty\":\"运营\",\"realNameStatus\":\"未实名\",\"baseServiceStatus\":\"未激活\",\"monitorPlatform\":\"英泰斯特\"},{\"id\":\"458755\",\"vin\":\"LS4ASE2E3JJ225949\",\"licensePlate\":\"新QLR012\",\"vehSeriesNo\":\"S301-18\",\"vehModelNo\":\"SC6469KBAH5\",\"vehModelName\":\"JL476ZQCF 1.5 国五6AT 6.5 STT\",\"vehConfNo\":\"SC6469KBAH5.S2\",\"vehProperty\":\"运营\",\"realNameStatus\":\"已实名\",\"baseServiceStatus\":\"已激活\",\"monitorPlatform\":\"英泰斯特\"},{\"id\":\"458756\",\"vin\":\"LS4ASE2E5JJ226844\",\"licensePlate\":\"新QLR013\",\"vehSeriesNo\":\"S301-18\",\"vehModelNo\":\"SC6469KBAH5\",\"vehModelName\":\"JL476ZQCF 1.5 国五6AT 6.5 STT\",\"vehConfNo\":\"SC6469KBAH5.S2\",\"vehProperty\":\"运营\",\"realNameStatus\":\"未实名\",\"baseServiceStatus\":\"未激活\",\"monitorPlatform\":\"英泰斯特\"},{\"id\":\"458759\",\"vin\":\"LS4ASE2E3JJ194671\",\"licensePlate\":\"川A56x1c\",\"vehSeriesNo\":\"S301PHEV\",\"vehModelNo\":\"SC6469GA5HEV\",\"vehModelName\":\"JL476ZQCG 国Ⅴ 电驱双电机 60(NEDC)\",\"vehConfNo\":\"SC6469GA5HEV.S72A\",\"vehProperty\":\"非运营\",\"realNameStatus\":\"未实名\",\"baseServiceStatus\":\"未激活\",\"monitorPlatform\":\"英泰斯特\"},{\"id\":\"458754\",\"vin\":\"LS4ASE2A4HJ117489\",\"licensePlate\":\"新QLR011\",\"vehSeriesNo\":\"S401\",\"vehModelNo\":\"SC6491AA5\",\"vehModelName\":\"JL486ZQ3 2.0T 国Ⅴ\",\"vehConfNo\":\"SC6491AA5.S91A\",\"vehProperty\":\"运营\",\"realNameStatus\":\"未实名\",\"baseServiceStatus\":\"未激活\",\"monitorPlatform\":\"英泰斯特\"},{\"id\":\"458757\",\"vin\":\"LS4ASE2E1JJ153066\",\"licensePlate\":\"新QLR014\",\"vehSeriesNo\":\"S301-18\",\"vehModelNo\":\"SC6469KBAH5\",\"vehModelName\":\"JL476ZQCF 1.5 国五6AT 6.5 STT\",\"vehConfNo\":\"SC6469KBAH5.S3\",\"vehProperty\":\"运营\",\"realNameStatus\":\"已实名\",\"baseServiceStatus\":\"已激活\",\"monitorPlatform\":\"英泰斯特\"},{\"id\":\"458750\",\"vin\":\"LS5A3DBE6HA014855\",\"licensePlate\":\"新QLR007\",\"vehSeriesNo\":\"C202\",\"vehModelNo\":\"SC7169G5\",\"vehModelName\":\"1.6L 5MT 国五二\",\"vehConfNo\":\"SC7169G5.C41B\",\"vehProperty\":\"运营\",\"realNameStatus\":\"未实名\",\"baseServiceStatus\":\"已激活\",\"monitorPlatform\":\"英泰斯特\"},{\"id\":\"458749\",\"vin\":\"LS4ASE2W9HJ126152\",\"licensePlate\":\"新QLR006\",\"vehSeriesNo\":\"S301\",\"vehModelNo\":\"SC6469B5\",\"vehModelName\":\"1.8T AT 国五\",\"vehConfNo\":\"SC6469B5.S31B\",\"vehProperty\":\"运营\",\"realNameStatus\":\"未实名\",\"baseServiceStatus\":\"未激活\",\"monitorPlatform\":\"英泰斯特\"},{\"id\":\"458752\",\"vin\":\"LS4ASE2A7GJ166524\",\"licensePlate\":\"新QLR009\",\"vehSeriesNo\":\"S301\",\"vehModelNo\":\"SC6469A5\",\"vehModelName\":\"2.0L MT 国五\",\"vehConfNo\":\"SC6469A5.S31B\",\"vehProperty\":\"运营\",\"realNameStatus\":\"未实名\",\"baseServiceStatus\":\"未激活\",\"monitorPlatform\":\"英泰斯特\"},{\"id\":\"458751\",\"vin\":\"LS4ASE2E3HJ268620\",\"licensePlate\":\"新QLR008\",\"vehSeriesNo\":\"S301\",\"vehModelNo\":\"SC6469DAA5\",\"vehModelName\":\"JL476ZQCA1.5T 国Ⅴ 6AT 油耗3\",\"vehConfNo\":\"SC6469DAA5.S22B\",\"vehProperty\":\"运营\",\"realNameStatus\":\"未实名\",\"baseServiceStatus\":\"已激活\",\"monitorPlatform\":\"英泰斯特\"}]},\"error\":false}"}  
	        };  
	    }  
	}  