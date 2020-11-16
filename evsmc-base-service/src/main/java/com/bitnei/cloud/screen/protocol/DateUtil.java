package com.bitnei.cloud.screen.protocol;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author xuzhijie
 */
public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public DateUtil() {
    }

    public static String getYesterday(DateFormatEnum format) {
        Date time = getDefineTime(5, -1);
        return convertDate2String(time, format);
    }

    public static String getLastMonth(DateFormatEnum format) {
        Date time = getDefineTime(2, -1);
        return convertDate2String(time, format);
    }

    public static String getLastMonth(String dateString, DateFormatEnum format) throws ParseException {
        Date date = convertString2Date(dateString, format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(2, -1);
        return convertDate2String(cal.getTime(), format);
    }

    public static String getLastYear(DateFormatEnum format) {
        Date time = getDefineTime(1, -1);
        return convertDate2String(time, format);
    }

    public static Date convertString2Date(String dateStr, DateFormatEnum dateFormat) throws ParseException {
        if (null != dateStr && !"".equals(dateStr.trim())) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat.getFormat());

            try {
                Date result = sdf.parse(dateStr);
                return result;
            } catch (ParseException var5) {
                logger.error(" #################### 字符串日期不合法:" + dateStr + " #################### ");
                throw new ParseException(var5.getMessage(), var5.getErrorOffset());
            }
        } else {
            return new Date();
        }
    }

    public static String convertDate2String(Date date, DateFormatEnum dateFormat) {
        if (null == date) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat.getFormat());
            String dateStr = sdf.format(date);
            return StringUtils.trimToEmpty(dateStr);
        }
    }

    public static Date getDefineTime(int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static String getMonthStartByDate(String dateString, DateFormatEnum format) throws ParseException {
        Date date = convertString2Date(dateString, format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return convertDate2String(calendar.getTime(), DateFormatEnum.DATE_TIME);
    }

    public static String getMonthEndByDate(String dateString, DateFormatEnum format) throws ParseException {
        Date date = convertString2Date(dateString, format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(5, calendar.getActualMaximum(5));
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        return convertDate2String(calendar.getTime(), DateFormatEnum.DATE_TIME);
    }

    public static Date getDateStartByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTime();
    }

    public static String getDateStartByDate(String dateString, DateFormatEnum format) throws ParseException {
        Date date = convertString2Date(dateString, format);
        Date startDate = getDateStartByDate(date);
        return convertDate2String(startDate, DateFormatEnum.DATE_TIME);
    }

    public static Date getDateEndByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        return calendar.getTime();
    }

    public static String getDateEndByDate(String dateString, DateFormatEnum format) throws ParseException {
        Date date = convertString2Date(dateString, format);
        Date endDate = getDateEndByDate(date);
        return convertDate2String(endDate, DateFormatEnum.DATE_TIME);
    }

    public static String getLateMonthDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(5, -30);
        return (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
    }
}