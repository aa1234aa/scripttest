package com.bitnei.apitest.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bitnei.apitest.annotation.pro.DataSource;
import com.bitnei.apitest.pro.WherePrams;
import com.bitnei.apitest.pro.datasendpro.TaskPro;

@Repository(value="taskdao")
@DataSource("platDataSource")
public class TaskDao extends DaoImpl<TaskPro, Integer>{
	public List<TaskPro> list(WherePrams where){
        return super.list(where);
    }
 
 public boolean isExist(WherePrams where){
        return super.isExist(where);
    }	
 
 public int addLocal(TaskPro pro){
     return super.addLocal(pro);
 }	
 
}
