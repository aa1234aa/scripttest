package com.bitnei.apitest.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.bitnei.apitest.dao.Dao;
import com.bitnei.apitest.pro.WherePrams;
import com.bitnei.apitest.pro.datasendpro.PlatFormPro;


@Repository(value="platformdao")
public class PlatFormDao extends DaoImpl<PlatFormPro, Integer> {
	

	 public List<PlatFormPro> list(WherePrams where){
	        return super.list(where);
	    }
	 
	 public boolean isExist(WherePrams where){
            return super.isExist(where);
        }	 
//
//	 public int del(WherePrams where){
//	        return super.del(where);
//	    }
//
	 public int add(PlatFormPro platformpro){
	        return super.add(platformpro);
	    }

//	public int update(PlatFormPro platformpro, WherePrams where) {
//		return super.update(platformpro);
//	}
}
