package com.bitnei.apitest.tool;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//配置多个数据源
public class DynamicDataSource extends AbstractRoutingDataSource{
	  @Override
	    protected Object determineCurrentLookupKey() {
	        return DataSourceContextHolder.getDbType();
	    }
}
