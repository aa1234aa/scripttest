package com.bitnei.util;

/*
@author 黄永雄
@create 2019/9/29 13:26
*/


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtil {

    static String window = "E:\\AutoFiles\\PJ2.3Scripts";
    static String linux = "/var/lib/jenkins/workspace/InterfaceAutomation2.3/PJ2.3Scripts";
    static String rootPath = PathUtil.getProjectRootPath(window, linux);
    static Properties prop = InitPropertiesUtil.init(rootPath + "/Resources/Conf/basicdata.properties");



    public static Connection getCon() {

        String dbUrl=prop.getProperty("dbUrl");
        String dbUserName=prop.getProperty("dbUserName");
        String dbPassword=prop.getProperty("dbPassword");
        String jdbcName=prop.getProperty("jdbcName");

        try {
            Class.forName(jdbcName);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            System.out.println("加载驱动失败");
        }
        Connection con=null;
        try {
            con = DriverManager.getConnection(dbUrl,dbUserName,dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("连接数据库失败");
        }
        return con;
    }
    public static  void closeCon(Connection con) throws Exception{
        if(con!=null){
            con.close();
        }
    }
}
