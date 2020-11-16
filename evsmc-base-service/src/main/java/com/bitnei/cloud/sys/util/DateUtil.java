package com.bitnei.cloud.sys.util;

import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 日期处理单元
 */
@Slf4j
public class DateUtil {
    public static final int SECOND = 1000;
    public static final int MINUTE = SECOND * 60;
    public static final int HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24L;
    public static final long WEEK = DAY * 7;
    public static final long YEAR = DAY * 356;

    static final public String FULL_ST_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static final public String FULL_J_FORMAT = "yyyy/MM/dd HH:mm:ss";
    static final public String CURRENCY_ST_FORMAT = "yyyy-MM-dd HH:mm";
    static final public String CURRENCY_J_FORMAT = "yyyy/MM/dd HH:mm";
    static final public String DATA_FORMAT = "yyyyMMddHHmmss";
    static final public String ST_FORMAT = "yyyy-MM-dd HH:mm";
    static final public String ST_CN_FORMAT = "yyyy年MM月dd日 HH:mm";
    static final public String CN_FORMAT = "yy年MM月dd日HH:mm";
    static final public String DAY_FORMAT = "yyyy-MM-dd";
    static final public String DAY_FORMAT_XIE = "yyyy/MM/dd";
    static final public String SHORT_DATE_FORMAT = "yy-MM-dd";
    static final public String YEAR_FORMAT = "yyyy";

    static final public String SHARDING_YEAR_MONTH = "yyyyMM";
    static final public String FULL_ST_FORMAT_MILL = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String getDate(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(FULL_ST_FORMAT);
        return sdf.format(new Date(timeStamp));
    }

    public static String getDateWithMill(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(FULL_ST_FORMAT_MILL);
        return sdf.format(new Date(timeStamp));
    }

    public static String getNow() {
        SimpleDateFormat sdf = new SimpleDateFormat(FULL_ST_FORMAT);
        return sdf.format(new Date());
    }

    public static String getNow(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        return sdf.format(new Date());
    }

    public static String getShortDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DAY_FORMAT);
        return sdf.format(new Date());
    }

    public static String getKafkaDataSyncTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT);
        return sdf.format(new Date());
    }

    public static String getKafkaDataSyncTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT);
        return sdf.format(date);
    }

    public static Date getStringToDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT);
        return sdf.parse(date);
    }

    public static Date strToDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DAY_FORMAT);
        return sdf.parse(date);
    }

    public static Date strToDate(String date, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(date);
    }

    public static String converseStr(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
           log.error("error", e);
            return "";
        }
        return formatTime(date, FULL_ST_FORMAT);
    }

    /**
     * 求两个时间中间间隔的天数
     *
     * @param time1
     * @param time2
     * @return
     */
    public static long getQuot(String time1, String time2) {
        long quot = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = ft.parse(time1);
            Date date2 = ft.parse(time2);
            quot = date1.getTime() - date2.getTime();
            quot = quot / 1000 / 60 / 60 / 24;
        } catch (Exception e) {
           log.error("error", e);
        }
        return Math.abs(quot);
    }

    public static String formatTime(Date date, String format) {
        if (date == null) {
            return "";
        }
        if (format.equals("")) {
            format = CURRENCY_ST_FORMAT;
        }

        SimpleDateFormat ft = new SimpleDateFormat(format);
        return ft.format(date);
    }

    public static long getLongFromDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FULL_ST_FORMAT);
        try {
            return sdf.parse(date).getTime();
        } catch (Exception e) {
            sdf = new SimpleDateFormat(SHORT_DATE_FORMAT);
            return sdf.parse(date).getTime();
        }

    }

    public static String dayPlus(String date, int n) {
        String ret = "";
        try {
            Date d1 = null;
            d1 = strToDate(date);
            Date d2 = new Date(d1.getTime() + (n * 86400000L));
            ret = DateUtil.formatTime(d2, DateUtil.DAY_FORMAT);
            return ret;
        } catch (ParseException e) {
           log.error("error", e);

        }
        return ret;

    }

    public static Date strToStr(String str, String str1) {
        SimpleDateFormat format = new SimpleDateFormat(str1);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
           log.error("error", e);
        }
        return date;

    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date, String str) {

        SimpleDateFormat format = new SimpleDateFormat(str);
        String str1 = format.format(date);
        return str1;
    }

    /**
     * 将时间字符串转换为对应格式的data
     *
     * @param str
     * @param formatter
     * @return
     */
    public static Date strToData(String str, String formatter) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        return sdf.parse(str);
    }

    /**
     * 获取当天的凌晨0点date
     *
     * @return
     */
    public static Date getNowDataStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 计算两个时间的差
     *
     * @param maxTime
     * @param minTime
     * @return
     */
    public static long getNowDataDifference(String maxTime, String minTime) {
        SimpleDateFormat dfs = new SimpleDateFormat(FULL_ST_FORMAT);
        Date begin = null;
        Date end = null;
        //分钟
        long between = 0;

        try {
            end = dfs.parse(maxTime);
            begin = dfs.parse(minTime);
            //分钟
            between = (end.getTime() - begin.getTime()) / 1000 / 60;
        } catch (ParseException e) {
           log.error("error", e);
        }


        return between;
    }

    public static long getDateDifferenceSec(String maxTime, String minTime) {
        SimpleDateFormat dfs = new SimpleDateFormat(FULL_ST_FORMAT);
        Date begin = null;
        Date end = null;

        long between = 0;

        try {
            end = dfs.parse(maxTime);
            begin = dfs.parse(minTime);
            //秒
            between = (end.getTime() - begin.getTime()) / 1000;
        } catch (ParseException e) {
           log.error("error", e);
        }


        return between;
    }

    /**
     * 秒转化为天小时分秒字符串
     *
     * @param seconds
     * @return String
     */
    public static String formatSeconds(long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }

    public static String formatSecondsToDayHour(long second) {
        //转换天数
        long days = second / 86400;
        //剩余秒数
        second = second % 86400;
        //转换小时
        long hours = second / 3600;

        String date = "";
        if (days > 0) {
            date += days + "天";
        }
        if (hours > 0) {
            date += hours + "小时";
        }
        if (StringUtils.isBlank(date) && second != 0) {
            date = "不足1小时";
        }
        return date;
    }

    public static Date addSecond(Date date, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, amount);
        date = calendar.getTime();
        return date;
    }

    public static Date addMinute(Date date, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, amount);
        date = calendar.getTime();
        return date;
    }

    public static Date addMonth(Date date, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, amount);
        date = calendar.getTime();
        return date;
    }

    public static Date addYear(Date date, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, amount);
        date = calendar.getTime();
        return date;
    }

    public static Date addDay(Date date, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, amount);
        return calendar.getTime();
    }

    /***
     * 判断两个时间差(浮点型)
     * @param type 1-天,2-小时,3-分钟,4-秒
     * @param startTime
     * @param endTime
     * @return
     */
    public static double getTimesDiffEx(int type, String startTime, String endTime) {
        if (!StringUtil.isEmpty(startTime) && !StringUtil.isEmpty(endTime)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long from = sdf.parse(startTime).getTime();
                long to = sdf.parse(endTime).getTime();
                double times = 0;
                if (type == 1) {
                    times = ((to - from) / 86400000d);
                } else if (type == 2) {
                    times = ((to - from) / 3600000d);
                } else if (type == 3) {
                    times = ((to - from) / 60000d);
                } else if (type == 4) {
                    times = ((to - from) / 1000d);
                }

                return times;
            } catch (ParseException var9) {
                log.error("error", var9);
            }
        }

        return -1;
    }



    /**
     * 严格的日期格式校验
     * @param dateStr 日期格式,支持yyyy-MM-dd,yyyy.MM.dd, yyyy/MM/dd
     * @return true:是 false:否
     */
    public static boolean validate(String dateStr) {
        if(org.apache.commons.lang3.StringUtils.isBlank(dateStr)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat();
        String pattern ;
        if(dateStr.contains("-")) {
            pattern = "yyyy-MM-dd";
        } else if (dateStr.contains(".")){
            pattern = "yyyy.MM.dd";
        } else if (dateStr.contains("/")){
            pattern = "yyyy/MM/dd";
        } else {
            return false;
        }
        try {
            //此处指定日期解析是否不严格
            sdf.setLenient(false);
            sdf.applyPattern(pattern);
            sdf.parse(dateStr);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * @param type 维度: 1:天  2:月
     * @param start
     * @param end
     * @return
     */
    public static List<String> getBetweenRange(int type, String start, String end) {

        List<String> result = new ArrayList<String>();
        try {
            SimpleDateFormat sdf;
            switch (type) {
                case 1: sdf = new SimpleDateFormat("yyyy-MM-dd"); break;
                case 2: sdf = new SimpleDateFormat("yyyy-MM"); break;
                default: sdf = new SimpleDateFormat("yyyy-MM-dd"); break;
            }
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(startDate);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(endDate);
            while (tempStart.before(tempEnd)||tempStart.equals(tempEnd)) {
                result.add(sdf.format(tempStart.getTime()));
                switch (type) {
                    case 1: tempStart.add(Calendar.DAY_OF_YEAR, 1); break;
                    case 2: tempStart.add(Calendar.MONTH, 1); break;
                    default: tempStart.add(Calendar.DAY_OF_YEAR, 1); break;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> range = getBetweenRange(2, "2019-06-20 00:00:00", "2019-09-23 00:00:00");
        range.forEach(it -> {
            System.out.println(it);
        });
    }

}
