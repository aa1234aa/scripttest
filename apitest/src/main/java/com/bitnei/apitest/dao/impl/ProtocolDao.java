package com.bitnei.apitest.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bitnei.apitest.pro.WherePrams;
import com.bitnei.apitest.pro.datasendpro.ProtocolPro;

@Repository(value="protocoldao")
public class ProtocolDao extends DaoImpl<ProtocolPro, Integer>{
	
	public List<ProtocolPro> list(WherePrams where){
        return super.list(where);
    }
 
 public boolean isExist(WherePrams where){
        return super.isExist(where);
    }	 

}
