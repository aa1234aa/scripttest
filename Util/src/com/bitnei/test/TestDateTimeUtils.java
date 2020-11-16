package com.bitnei.test;

import com.bitnei.util.DateTimeUtils;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

public class TestDateTimeUtils extends TestCase {


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

        String s7=DateTimeUtils.getCurrentLongDateTimeStr();
        System.out.println(s7);

    }

    @Test
    public void testGetCurrentAddTime(){

        LocalDateTime l1=DateTimeUtils.getCurrentAddSecondTime(3);
        LocalDateTime l2=DateTimeUtils.getCurrentAddMinutesTime(3);
        LocalDateTime l3=DateTimeUtils.getCurrentAddHourTime(3);
        LocalDateTime l4=DateTimeUtils.getCurrentAddDateTime(3);
        LocalDateTime l5=DateTimeUtils.getCurrentAddMonthsTime(3);
        LocalDateTime l6=DateTimeUtils.getCurrentAddYearsTime(3);
        String s1=DateTimeUtils.formatLocalDateTime(l1);
        String s2=DateTimeUtils.formatLocalDateTime(l2);
        String s3=DateTimeUtils.formatLocalDateTime(l3);
        String s4=DateTimeUtils.formatLocalDateTime(l4);
        String s5=DateTimeUtils.formatLocalDateTime(l5);
        String s6=DateTimeUtils.formatLocalDateTime(l6);
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);
        System.out.println(s5);
        System.out.println(s6);
    }


}
