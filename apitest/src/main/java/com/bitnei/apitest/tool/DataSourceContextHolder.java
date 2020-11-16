package com.bitnei.apitest.tool;

public class DataSourceContextHolder {
	
	/**
	 * 注意：数据源标识保存在线程变量中，避免多线程操作数据源时互相干扰
	 */
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	
	public static void setDbType(String dbType){
		contextHolder.set(dbType);
	}
	
	public static String getDbType(){
		return contextHolder.get();
	}
	
	public static void clearDbType(){
		contextHolder.remove();
	}


}
