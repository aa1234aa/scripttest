package com.bitnei.test;

import com.bitnei.util.DateTimeUtil;
import com.bitnei.util.DbUtil;
import com.bitnei.util.RandomValueUtil;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Map;

public class TestDateTimeUtil extends TestCase {


    @Before
    public void setUp() throws Exception {
        super.setUp();
     
    }

    @After
    public void tearDown() throws Exception {
        
        super.tearDown();
    
    }

    @Test
    public void testGetCurrentLongDateTimeStr() {

        String s7= DateTimeUtil.getCurrentLongDateTimeStr();
        System.out.println(s7);

    }

    @Test
    public void testGetCurrentAddTime(){

        LocalDateTime l1= DateTimeUtil.getCurrentAddSecondTime(3);
        LocalDateTime l2= DateTimeUtil.getCurrentAddMinutesTime(3);
        LocalDateTime l3= DateTimeUtil.getCurrentAddHourTime(3);
        LocalDateTime l4= DateTimeUtil.getCurrentAddDateTime(3);
        LocalDateTime l5= DateTimeUtil.getCurrentAddMonthsTime(3);
        LocalDateTime l6= DateTimeUtil.getCurrentAddYearsTime(3);
        String s1= DateTimeUtil.formatLocalDateTime(l1);
        String s2= DateTimeUtil.formatLocalDateTime(l2);
        String s3= DateTimeUtil.formatLocalDateTime(l3);
        String s4= DateTimeUtil.formatLocalDateTime(l4);
        String s5= DateTimeUtil.formatLocalDateTime(l5);
        String s6= DateTimeUtil.formatLocalDateTime(l6);
        Map<String,String> addressList= RandomValueUtil.getAddress();
        String stf=addressList.get("road");
        String address= RandomValueUtil.getRoad();
        System.out.println(address);
        System.out.println(stf);
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);
        System.out.println(s5);
        System.out.println(s6);
    }

    @Test
    public void testDbUtil() throws Exception {

        String sql=" SELECT id FROM sys_sim_management  ORDER BY create_time DESC LIMIT 1 ";

        Connection conn=DbUtil.getCon();
        Statement stmt=conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql) ;


        while(rs.next()){
            String id=rs.getString("id");
            System.out.println(id);
        }

        DbUtil.closeCon(conn);

    }


}
