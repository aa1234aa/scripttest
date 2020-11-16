package com.bitnei.apitest.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bitnei.apitest.pro.WherePrams;
import com.bitnei.apitest.pro.datasendpro.ForwardVehiclePro;

@Repository(value="forwardvehicledao")
public class ForwardVehicleDao extends DaoImpl<ForwardVehiclePro, Integer>{
	
	public List<ForwardVehiclePro> list(WherePrams where){
        return super.list(where);
    }
 
 public boolean isExist(WherePrams where){
        return super.isExist(where);
    }	

}
