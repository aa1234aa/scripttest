package com.bitnei.cloud.sys.util;

/*
@author 黄永雄
@create 2019/11/12 14:17
*/


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DbUtilsTest {




    private Connection conn;
    private Statement stmt;
    private ResultSet rs ;


    @Test
    public void testDbUtil() throws Exception {

        DbUtil dbUtil= new DbUtil();

        String sql=" SELECT id FROM sys_owner_people WHERE owner_name='于莲' ";
        conn=dbUtil.getCon();
        stmt=conn.createStatement();
        rs = stmt.executeQuery(sql) ;

        while(rs.next()){
            String id=rs.getString("id");
            System.out.println(id);
        }

        dbUtil.closeCon(conn);

    }


}
