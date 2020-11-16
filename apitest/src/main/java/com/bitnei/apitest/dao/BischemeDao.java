//package com.bitnei.apitest.dao;
//
//import javax.annotation.Resource;
//
//import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.stereotype.Repository;
//
//import com.bitnei.apitest.dao.impl.DaoImpl;
//import com.bitnei.apitest.pro.BischemePro;
//
//@Repository
//public class BischemeDao extends Dao<BischemePro, Integer> {
//
////	@Resource
////	 private SqlSessionTemplate sqlSessionTemplateASS;
//
//	 /**
//	  * 重载Add方法，批量添加
//	  * @param list
//	  * @return
//	  */
////	 public int add(List<TestUserPo> list){
////
////	  long startId = nextId();
////
////	  for (int i = 0; i < list.size(); i++) {
////	   list.get(i).setId(Integer.valueOf(startId + i + ""));
////	  }
////
////	  //注意了，这里的“user_addList”对应的是userDaoMapper.xml中的insertSQL块的Id
////	  return sqlSessionTemplateASS.insert("user_addList", list);
////	 }
//	
//
//
//}
