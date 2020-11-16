package com.bitnei.cloud.sys.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

    private Connection con=null;


    public  Connection getCon() {

        String dbUrl="jdbc:mysql://192.168.1.108:3306/evsmc2?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useAffectedRows=true";
        String dbUserName="evsmc2";
        String dbPassword="evsmc2";
        String jdbcName="com.mysql.jdbc.Driver";


        try {
            Class.forName(jdbcName);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            System.out.println("加载驱动失败");
        }

        try {
            con = DriverManager.getConnection(dbUrl,dbUserName,dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("连接数据库失败");
        }
        return con;
    }
    public void closeCon(Connection con) throws Exception{
        if(con!=null){
            con.close();
        }
    }

}