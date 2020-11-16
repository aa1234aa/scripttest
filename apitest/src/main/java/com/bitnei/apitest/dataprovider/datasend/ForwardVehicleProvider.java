package com.bitnei.apitest.dataprovider.datasend;

import org.testng.annotations.DataProvider;

public class ForwardVehicleProvider {
	
	 @DataProvider(name="dataprovider1")  
	    public static Object[][] dataProvider1(){  
	        return new Object[][]{  
	        	{"{\"status\":200,\"errorMsg\":null,\"data\":{\"total\":1,\"list\":[{\"id\":662855,\"taskId\":50,\"vehicleId\":\"458763\",\"vin\":\"LS4ASE2A1HJ121001\",\"licensePlate\":\"川JHK979\",\"vehType\":\"私家车\",\"vehModelNo\":\"SC6491B5\",\"vendor\":\"北京长安汽车有限公司\",\"operatingCompany\":\"长安\",\"communicationProtocol\":\"1\",\"city\":\"北京市\",\"taskName\":\"testhg0227144\",\"platformName\":\"testhg022716\",\"platformId\":\"a2a51080-3a63-11e9-9869-90e2bae77ed8\",\"protocolId\":\"6d8b1c1e-d7f7-11e8-a918-6c92bf645824\",\"protocolName\":\"国标32960\",\"monitorPlatform\":\"1\",\"tenantName\":\"chan_tenant_name\"}],\"pageNum\":1,\"pageSize\":10,\"size\":1,\"startRow\":1,\"endRow\":1,\"pages\":1,\"prePage\":0,\"nextPage\":0,\"isFirstPage\":true,\"isLastPage\":true,\"hasPreviousPage\":false,\"hasNextPage\":false,\"navigatePages\":8,\"navigatepageNums\":[1],\"navigateFirstPage\":1,\"navigateLastPage\":1,\"firstPage\":1,\"lastPage\":1},\"error\":false}"}
	        };
	    }
	 
	 @DataProvider(name="dataprovider2")  
	    public static Object[][] dataProvider2(){  
	        return new Object[][]{  
	        	{"{\"status\":200,\"errorMsg\":null,\"data\":{\"total\":1,\"list\":[{\"id\":353859,\"taskId\":22,\"vehicleId\":\"458750\",\"vin\":\"LS5A3DBE6HA014855\",\"licensePlate\":\"新QLR007\",\"vehType\":\"私家车\",\"vehModelNo\":\"SC7169G5\",\"vendor\":\"重庆长安汽车有限公司\",\"operatingCompany\":\"长安\",\"communicationProtocol\":\"1\",\"city\":\"北京市\",\"taskName\":\"任务1\",\"platformName\":\"合肥长安国家平台\",\"platformId\":\"3328f1f3-d8e6-11e8-a918-6c92bf645824\",\"protocolId\":\"6d8b1c1e-d7f7-11e8-a918-6c92bf645824\",\"protocolName\":\"国标32960\",\"monitorPlatform\":\"2\"}],\"pageNum\":1,\"pageSize\":10,\"size\":1,\"startRow\":1,\"endRow\":1,\"pages\":1,\"prePage\":0,\"nextPage\":0,\"isFirstPage\":true,\"isLastPage\":true,\"hasPreviousPage\":false,\"hasNextPage\":false,\"navigatePages\":8,\"navigatepageNums\":[1],\"navigateFirstPage\":1,\"navigateLastPage\":1,\"firstPage\":1,\"lastPage\":1},\"error\":false}"}
	        };
	    }
	 
	 @DataProvider(name="dataprovider3")  
	    public static Object[][] dataProvider3(){  
	        return new Object[][]{  
	        	{"{\"status\":200,\"errorMsg\":null,\"data\":{\"total\":1,\"list\":[{\"id\":458763,\"vin\":\"LS4ASE2A1HJ121001\",\"licensePlate\":\"川JHK979\",\"vehType\":\"私家车\",\"vehModelNo\":\"SC6491B5\",\"vendor\":\"北京长安汽车有限公司\",\"operatingCompany\":\"长安\",\"city\":\"北京市\",\"province\":\"北京市\",\"monitorPlatform\":\"1\",\"operationCity\":\"北京市\",\"vehName\":\"我的爱车\",\"vehSeriesNo\":\"S401\",\"vehSeriesName\":\"CS95\",\"vehModelName\":\"JL486ZQ3 2.0T 国Ⅴ 6AT 7座\",\"vehConfNo\":\"SC6491B5.S92A\",\"vehConfName\":\"9 商品车 L12 北京产 四驱7座\",\"motorModelNo\":\"H9BJ020997\",\"vehColor\":\"闪光月光白\",\"purchaseDate\":\"2017-10-23 00:00:00\",\"manufactureDate\":\"2017-08-28 00:00:00\",\"registDate\":\"2017-10-23 15:32:34\",\"activeDate\":\"2018-12-12 17:08:20\",\"dealer\":\"遂宁骏骐汽车有限公司\",\"baseServiceStatus\":1,\"serviceStartDate\":\"2017-10-24 09:52:05\",\"serviceExpireDate\":\"2027-10-24 09:52:05\",\"maskCode\":\"111\",\"telephone\":\"15828897551\",\"credentials\":\"身份证\",\"credentialsNo\":\"1\",\"realNameStatus\":1,\"owner\":\"辜良\",\"realNameDate\":\"2017-10-24 09:52:05\",\"infoSource\":\"monitor\",\"createDate\":\"2017-10-23 15:32:34\",\"createrId\":\"2010093000861440\",\"firstOnlineTime\":\"2018-12-12 17:08:44\",\"flushTime\":\"2018-12-16 13:05:30\",\"terminalNo\":\"FOURG_TBOX\",\"terminalBrand\":\"法雷奥集团\",\"tenantId\":\"CA_PV_TENANT\",\"tenantName\":\"chan_tenant_name\",\"vehProperty\":\"非运营\",\"tuid\":\"3.20010011707171E31\",\"ecph\":\"111\",\"dept\":\"私家车\",\"carId\":\"ff1f77ce-1b26-4b32-a916-5d7d6a94b826\"}],\"pageNum\":1,\"pageSize\":10,\"size\":1,\"startRow\":1,\"endRow\":1,\"pages\":1,\"prePage\":0,\"nextPage\":0,\"isFirstPage\":true,\"isLastPage\":true,\"hasPreviousPage\":false,\"hasNextPage\":false,\"navigatePages\":8,\"navigatepageNums\":[1],\"navigateFirstPage\":1,\"navigateLastPage\":1,\"firstPage\":1,\"lastPage\":1},\"error\":false}"}
	        };
	    }
	 
	 @DataProvider(name="dataprovider4")  
	    public static Object[][] dataProvider4(){  
	        return new Object[][]{  
	        	{"{\"status\":200,\"errorMsg\":null,\"data\":null,\"error\":false}"}
	        };
	    }
	 
}
