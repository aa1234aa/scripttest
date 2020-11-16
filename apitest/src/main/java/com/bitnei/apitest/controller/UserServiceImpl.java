//package com.bitnei.apitest.controller;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Service;
//
//import com.bitnei.apitest.dao.UserDao;
//import com.bitnei.apitest.pro.Method;
//import com.bitnei.apitest.pro.datasendpro.PlatFormPro;
//import com.bitnei.apitest.sql.C;
//
//@Service
//public class UserServiceImpl{
//
//    @Resource
//    private UserDao userDao;
//
//        /*
//        *根据姓名查找用户
//        */
//    public List<PlatFormPro> listUser(String name){
//        return userDao.list(Method.where("unitName", C.LIKE, name));
//    }
//}
//
