package com.bitnei.apitest.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bitnei.apitest.annotation.pro.DataSource;
import com.bitnei.apitest.pro.WherePrams;
import com.bitnei.apitest.pro.bidash.SchemePro;

@Repository(value="schemedao")
@DataSource("biDataSource")
public class SchemeDao extends DaoImpl<SchemePro, Integer>{

	public List<SchemePro> list(WherePrams where){
        return super.list(where);
    }
 
    public boolean isExist(WherePrams where){
        return super.isExist(where);
    }	

}
