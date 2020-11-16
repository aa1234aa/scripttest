package com.bitnei.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtils {
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");
    public static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter YEARMONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");
    public static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
    public static final DateTimeFormatter lONG_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter SHORT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter LONG_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");
    public static final DateTimeFormatter DATE_START_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");
    public static final DateTimeFormatter DATE_END_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59");

    /**
     * 返回LocalDate格式的：当前的日期yy-MM-dd  2019-09-26
     */
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    /**
     * 返回LocalTime格式的当前时间： HH:mm:ss.SSS 13:42:27.816
     */

    public static LocalTime getCurrentLocalTime() {

        return LocalTime.now();


    }

    /**
     * 返回 LocalDateTime格式的当前日期时间：yyyy-MM-ddTHH:mm:ss.SSS 2019-09-26T13:42:27.816
     */

    public static LocalDateTime getCurrentLocalDateTime() {

        return LocalDateTime.now();
    }


    /**
     * 返回 LocalDateTime格式的当前时间，增减N天
     *
     */
    public static LocalDateTime getCurrentAddDateTime(int num) {

        return LocalDateTime.now().minus(num, ChronoUnit.DAYS);
    }

    /**
     * 返回LocalDateTime格式的当前时间，增减N小时
     * *
     */
    public static LocalDateTime getCurrentAddHourTime(int num) {

        return LocalDateTime.now().minus(num, ChronoUnit.HOURS);
    }

    /**
     * 返回LocalDateTime格式的当前时间，增减N秒
     * *
     */
    public static LocalDateTime getCurrentAddSecondTime(int num) {

        return LocalDateTime.now().minus(num, ChronoUnit.SECONDS);
    }

    /**
     * 返回LocalDateTime格式的当前时间，增减N分
     * *
     */
    public static LocalDateTime getCurrentAddMinutesTime(int num) {

        return LocalDateTime.now().minus(num, ChronoUnit.MINUTES);
    }

    /**
     * 返回LocalDateTime格式的当前时间，增减N月
     * *
     */
    public static LocalDateTime getCurrentAddMonthsTime(int num) {

        return LocalDateTime.now().minus(num, ChronoUnit.MONTHS);
    }

    /**
     * 返回LocalDateTime格式的当前时间，增减N年
     * *
     */
    public static LocalDateTime getCurrentAddYearsTime(int num) {

        return LocalDateTime.now().minus(num, ChronoUnit.YEARS);
    }


    /**
     * 返回LocalDateTime格式的当前时间，增减N周
     * *
     */
    public static LocalDateTime getCurrentAddWeeksTime(int num) {

        return LocalDateTime.now().minus(num, ChronoUnit.WEEKS);
    }


    /**
     * 返回字符串的当前时间：yyyy-MM-dd  2019-09-26
     */

    public static String getCurrentDateStr() {

        return LocalDate.now().format(DATE_FORMATTER);
    }

    /**
     * 返回字符串的当前时间：yyMMdd 190926
     */
    public static String getCurrentShortDateStr() {

        return LocalDate.now().format(SHORT_DATE_FORMATTER);
    }

    /**
     * 返回字符串的当前时间：yyyyMMdd 20190926
     */

    public static String getCurrentLongDateStr() {

        return LocalDate.now().format(lONG_DATE_FORMATTER );
    }

    /**
     *
     * 返回字符串的当前时间：yyyy-MM 2019-09
     */

    public static String getCurrentMonthStr() {

        return LocalDate.now().format(YEAR_MONTH_FORMATTER);
    }

    /**
     * 返回字符串的当前时间无杠号：yyyyMM 201909
     */

    public static String getCurrentYearMonthStr() {

        return LocalDate.now().format(YEARMONTH_FORMATTER);
    }

    /**
     * 返回字符串的当前时间：yyyy-MM-dd HH:mm:ss 2019-09-26 13:48:50
     */
    public static String getCurrentDateTimeStr() {

        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }

    /**
     *
     * 返回字符串的当前时间：yyyy-MM-dd HH:mm:ss SSS 2019-09-26 14:25:36 506
     */

    public static String getCurrentLongDateTimeStr(){

        return LocalDateTime.now().format(LONG_DATETIME_FORMATTER);
    }

    /**
     * 返回字符串的当前时间：yyMMddHHmmss 190926142830
     */
    public static String getCurrentShortDateTimeStr() {

        return LocalDateTime.now().format(SHORT_DATETIME_FORMATTER);
    }

    /**
     * 返回字符串的当前时间：HHmmss 143032
     */
    public static String getCurrentTimeStr() {

        return LocalTime.now().format(TIME_FORMATTER);
    }

    /**
     * 返回字符串的当前时间的最早开始时间: yyyy-MM-dd 00:00:00  2019-09-26 00:00:00
     */
    public static String getStartDateTimeStr() {

        return LocalDateTime.now().format(DATE_START_TIME_FORMATTER);
    }

    /**
     * 返回字符串的当前时间的最晚结束时间: yyyy-MM-dd 23:59:59  2019-09-26 23:59:59
     */
    public static String getEndDateTimeStr() {

        return LocalDateTime.now().format(DATE_END_TIME_FORMATTER);
    }



    public static String getCurrentDateStr(String pattern) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getCurrentDateTimeStr(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getCurrentTimeStr(String pattern) {
        return LocalTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalTime parseLocalTime(String timeStr, String pattern) {
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalDateTime(LocalDateTime datetime, String pattern) {
        return datetime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalTime(LocalTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseLocalDate(String dateStr) {

        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr) {

        return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
    }

    public static LocalDateTime parseLongLocalDateTime(String longDateTimeStr){

        return LocalDateTime.parse(longDateTimeStr, LONG_DATETIME_FORMATTER);
    }

    public static LocalTime parseLocalTime(String timeStr) {

        return LocalTime.parse(timeStr, TIME_FORMATTER);
    }

    public static String formatLocalDate(LocalDate date) {

        return date.format(DATE_FORMATTER);
    }

    public static String formatLocalDateTime(LocalDateTime datetime) {

        return datetime.format(DATETIME_FORMATTER);
    }

    public static String formatLocalDateStartTime(LocalDateTime datetime) {

        return datetime.format(DATE_START_TIME_FORMATTER);
    }

    public static String formatLocalDateEndTime(LocalDateTime datetime) {

        return datetime.format(DATE_END_TIME_FORMATTER);
    }

    public static String formatLocalTime(LocalTime time) {

        return time.format(TIME_FORMATTER);
    }

    /**
     * 日期相隔秒
     */
    public static long periodHours(LocalDateTime startDateTime,LocalDateTime endDateTime){

        return Duration.between(startDateTime, endDateTime).get(ChronoUnit.SECONDS);
    }

    /**
     * 日期相隔天数
     */
    public static long periodDays(LocalDate startDate, LocalDate endDate) {

        return startDate.until(endDate, ChronoUnit.DAYS);
    }

    /**
     * 日期相隔周数
     */
    public static long periodWeeks(LocalDate startDate, LocalDate endDate) {

        return startDate.until(endDate, ChronoUnit.WEEKS);
    }

    /**
     * 日期相隔月数
     */
    public static long periodMonths(LocalDate startDate, LocalDate endDate) {

        return startDate.until(endDate, ChronoUnit.MONTHS);
    }

    /**
     * 日期相隔年数
     */
    public static long periodYears(LocalDate startDate, LocalDate endDate) {

        return startDate.until(endDate, ChronoUnit.YEARS);
    }

    /**
     * 是否当天
     */
    public static boolean isToday(LocalDate date) {

        return getCurrentLocalDate().equals(date);
    }
    /**
     * 获取当前毫秒数
     */
    public static Long toEpochMilli(LocalDateTime dateTime) {

        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 判断是否为闰年
     */
    public static boolean isLeapYear(LocalDate localDate){

        return localDate.isLeapYear();
    }

}
