package com.lin.cache.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具
 * @author lin
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

	public static final String TIME_WITH_MINUTE_PATTERN = "HH:mm";
	
	public static final long DAY_MILLI = 24 * 60 * 60 * 1000; // 一天的MilliSecond
	
	public final static int LEFT_OPEN_RIGHT_OPEN = 1;
	
	/**
	 * 要用到的DATE Format的定义
	 */
	public static String DATE_FORMAT_DATEONLY = "yyyy-MM-dd"; // 年/月/日
	public static String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss"; // 年/月/日
	public static SimpleDateFormat sdfDateTime = new SimpleDateFormat(DateUtils.DATE_FORMAT_DATETIME);
	// Global SimpleDateFormat object
	public static SimpleDateFormat sdfDateOnly = new SimpleDateFormat(DateUtils.DATE_FORMAT_DATEONLY);
	public static final SimpleDateFormat SHORTDATEFORMAT = new SimpleDateFormat("yyyyMMdd");
	public static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat HMS_FORMAT = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat formatTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	/**
	 * 根据日期格式字符串解析日期字符串
	 * @param str 日期字符串
	 * @param parsePatterns 日期格式字符串
	 * @return 解析后日期
	 * @throws ParseException 
	 */
	public static Date parseDate(String str, String parsePatterns) throws ParseException {
		return parseDate(str, new String[]{parsePatterns});
	}
	
	/**
	 * 根据单位字段比较两个日期
	 * @param date 日期1
	 * @param otherDate 日期2
	 * @param withUnit 单位字段，从Calendar field取值
	 * @return 等于返回0值, 大于返回大于0的值 小于返回小于0的值
	 */
	public static int compareDate(Date date, Date otherDate, int withUnit) {
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(date);
        Calendar otherDateCal = Calendar.getInstance();
        otherDateCal.setTime(otherDate);
        
        switch (withUnit) {
        case Calendar.YEAR:
        	dateCal.clear(Calendar.MONTH);
        	otherDateCal.clear(Calendar.MONTH);
        case Calendar.MONTH:
        	dateCal.set(Calendar.DATE, 1);
        	otherDateCal.set(Calendar.DATE, 1);
        case Calendar.DATE:
        	dateCal.set(Calendar.HOUR_OF_DAY, 0);
        	otherDateCal.set(Calendar.HOUR_OF_DAY, 0);
        case Calendar.HOUR:
        	dateCal.clear(Calendar.MINUTE);
        	otherDateCal.clear(Calendar.MINUTE);
        case Calendar.MINUTE:
        	dateCal.clear(Calendar.SECOND);
        	otherDateCal.clear(Calendar.SECOND);
        case Calendar.SECOND:
        	dateCal.clear(Calendar.MILLISECOND);
        	otherDateCal.clear(Calendar.MILLISECOND);
        case Calendar.MILLISECOND:
			break;
		default: throw new IllegalArgumentException("withUnit 单位字段 " + withUnit + " 不合法！！");
		}
		return dateCal.compareTo(otherDateCal);
	}
	
	/**
	 * 根据单位字段比较两个时间
	 * @param date 时间1
	 * @param otherDate 时间2
	 * @param withUnit 单位字段，从Calendar field取值
	 * @return 等于返回0值, 大于返回大于0的值 小于返回小于0的值
	 */
	public static int compareTime(Date date, Date otherDate, int withUnit) {
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(date);
		Calendar otherDateCal = Calendar.getInstance();
        otherDateCal.setTime(otherDate);
        
        dateCal.clear(Calendar.YEAR);
        dateCal.clear(Calendar.MONTH);
        dateCal.set(Calendar.DATE, 1);
        otherDateCal.clear(Calendar.YEAR);
        otherDateCal.clear(Calendar.MONTH);
    	otherDateCal.set(Calendar.DATE, 1);
        switch (withUnit) {
        case Calendar.HOUR:
        	dateCal.clear(Calendar.MINUTE);
        	otherDateCal.clear(Calendar.MINUTE);
        case Calendar.MINUTE:
        	dateCal.clear(Calendar.SECOND);
        	otherDateCal.clear(Calendar.SECOND);
        case Calendar.SECOND:
        	dateCal.clear(Calendar.MILLISECOND);
        	otherDateCal.clear(Calendar.MILLISECOND);
        case Calendar.MILLISECOND:
			break;
		default: throw new IllegalArgumentException("withUnit 单位字段 " + withUnit + " 不合法！！");
		}
        return dateCal.compareTo(otherDateCal);
	}
	
	/**
	 * 获得当前的日期毫秒
	 * @return
	 */
	public static long nowTimeMillis(){
		return System.currentTimeMillis();
	}
	/**
	 * 获得当前的时间戳
	 * @return
	 */
	public static Timestamp nowTimeStamp() {
		return new Timestamp(nowTimeMillis());
	}
	/**
	 * yyyyMMdd 当前日期
	 * 
	 */
	public static String getReqDate() {
		return SHORTDATEFORMAT.format(new Date());
	}
	/**
	 * yyyy-MM-dd 传入日期
	 * @param date
	 * @return
	 */
	public static String getReqDate(Date date) {
		return SHORT_DATE_FORMAT.format(date);
	}
	/**
	 * yyyy-MM-dd  传入的时间戳
	 * @param date
	 * @return
	 */
	public static String TimestampToDateStr(Timestamp tmp) {
		return SHORT_DATE_FORMAT.format(tmp);
	}
	/**
	 * HH:mm:ss 当前时间
	 * @return
	 */
	public static String getReqTime() {
		return HMS_FORMAT.format(new Date());
	}
	/**
	 * 得到时间戳格式字串 
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeStampStr(Date date) {
		return LONG_DATE_FORMAT.format(date);
	}
	/**
	 * 得到长日期格式字串
	 * 
	 * @return
	 */
	public static String getLongDateStr() {
		return LONG_DATE_FORMAT.format(new Date());
	}
	public static String getLongDateStr(Timestamp time) {
		return LONG_DATE_FORMAT.format(time);
	}
	/**
	 * 得到短日期格式字串
	 * 
	 * @param date
	 * @return
	 */
	public static String getShortDateStr(Date date) {
		return SHORT_DATE_FORMAT.format(date);
	}
 
	public static String getShortDateStr() {
		return SHORT_DATE_FORMAT.format(new Date());
	}
	
	/**
	 * 计算 second 秒后的时间
	 * 
	 * @param date
	 * @param second
	 * @return
	 */
	public static Date addSecond(Date date, int second){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);;
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}
	
	/**
	 * 计算 minute 分钟后的时间
	 * 
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date addMinute(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}
	/**
	 * 计算 hour 小时后的时间
	 * 
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date addHour(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hour);
		return calendar.getTime();
	}
	/**
	 * 得到day的起始时间点。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	/**
	 * 得到day的终止时间点.
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}
	/**
	 * 计算 day 天后的时间
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	/**
	 * 得到month的终止时间点.
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}
	public static Date addYear(Date date, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 365*year);
		return calendar.getTime();
	}
	public static Timestamp strToTimestamp(String dateStr){
		return Timestamp.valueOf(dateStr);
	}
	public static Timestamp strToTimestamp(Date date){
        return Timestamp.valueOf(formatTimestamp.format(date)); 
	}
	public static Timestamp getCurTimestamp(){
        return Timestamp.valueOf(formatTimestamp.format(new Date())); 
	}

	/**
	 * 取得两个日期之间的日数
	 * 
	 * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
	 */
	public static long daysBetween(java.sql.Timestamp t1, java.sql.Timestamp t2) {
		return (t2.getTime() - t1.getTime()) / DAY_MILLI;
	}
	/**
	 * 返回java.sql.Timestamp型的SYSDATE
	 * 
	 * @return java.sql.Timestamp型的SYSDATE
	 * @since 1.0
	 * @history
	 */
	public static java.sql.Timestamp getSysDateTimestamp() {
		return new java.sql.Timestamp(System.currentTimeMillis());
	}
	/**
	 * 利用缺省的Date格式(YYYY/MM/DD)转换String到java.sql.Timestamp
	 * 
	 * @param sDate
	 *            Date string
	 * @return
	 * @since 1.0
	 * @history
	 */
	public static java.sql.Timestamp toSqlTimestamp(String sDate) {
		if (sDate == null) {
			return null;
		}
		if (sDate.length() != DateUtils.DATE_FORMAT_DATEONLY.length()) {
			return null;
		}
		return toSqlTimestamp(sDate, DateUtils.DATE_FORMAT_DATEONLY);
	}
	/**
	 * 利用缺省的Date格式(YYYY/MM/DD hh:mm:ss)转化String到java.sql.Timestamp
	 * 
	 * @param sDate
	 *            Date string
	 * @param sFmt
	 *            Date format DATE_FORMAT_DATEONLY/DATE_FORMAT_DATETIME
	 * @return
	 * @since 1.0
	 * @history
	 */
	public static java.sql.Timestamp toSqlTimestamp(String sDate, String sFmt) {
		String temp = null;
		if (sDate == null || sFmt == null) {
			return null;
		}
		if (sDate.length() != sFmt.length()) {
			return null;
		}
		if (sFmt.equals(DateUtils.DATE_FORMAT_DATETIME)) {
			temp = sDate.replace('/', '-');
			temp = temp + ".000000000";
		} else if (sFmt.equals(DateUtils.DATE_FORMAT_DATEONLY)) {
			temp = sDate.replace('/', '-');
			temp = temp + " 00:00:00.000000000";
			// }else if( sFmt.equals (DateUtils.DATE_FORMAT_SESSION )){
			// //Format: 200009301230
			// temp =
			// sDate.substring(0,4)+"-"+sDate.substring(4,6)+"-"+sDate.substring(6,8);
			// temp += " " + sDate.substring(8,10) + ":" +
			// sDate.substring(10,12) + ":00.000000000";
		} else {
			return null;
		}
		// java.sql.Timestamp.value() 要求的格式必须为yyyy-mm-dd hh:mm:ss.fffffffff
		return java.sql.Timestamp.valueOf(temp);
	}
	/**
	 * 以YYYY/MM/DD HH24:MI:SS格式返回系统日期时间
	 * 
	 * @return 系统日期时间
	 * @since 1.0
	 * @history
	 */
	public static String getSysDateTimeString() {
		return toString(new java.util.Date(System.currentTimeMillis()),
				DateUtils.sdfDateTime);
	}
	/**
	 * 根据指定的Format转化java.util.Date到String
	 * 
	 * @param dt
	 *            java.util.Date instance
	 * @param sFmt
	 *            Date format , DATE_FORMAT_DATEONLY or DATE_FORMAT_DATETIME
	 * @return
	 * @since 1.0
	 * @history
	 */
	public static String toString(java.util.Date dt, String sFmt) {
		if (dt == null || sFmt == null || "".equals(sFmt)) {
			return "";
		}
		return toString(dt, new SimpleDateFormat(sFmt));
	}

	/**
	 * 利用指定SimpleDateFormat instance转换java.util.Date到String
	 * 
	 * @param dt
	 *            java.util.Date instance
	 * @param formatter
	 *            SimpleDateFormat Instance
	 * @return
	 * @since 1.0
	 * @history
	 */
	private static String toString(java.util.Date dt, SimpleDateFormat formatter) {
		String sRet = null;

		try {
			sRet = formatter.format(dt).toString();
		} catch (Exception e) {
			e.printStackTrace();
			sRet = null;
		}

		return sRet;
	}
	/**
	 * 转换java.sql.Timestamp到String，格式为YYYY/MM/DD HH24:MI
	 * 
	 * @param dt
	 *            java.sql.Timestamp instance
	 * @return
	 * @since 1.0
	 * @history
	 */
	public static String toSqlTimestampString2(java.sql.Timestamp dt) {
		if (dt == null) {
			return null;
		}
		String temp = toSqlTimestampString(dt, DateUtils.DATE_FORMAT_DATETIME);
		return temp.substring(0, 16);
	}
	public static String toString(java.sql.Timestamp dt) {
		return dt == null ? "" : toSqlTimestampString2(dt);
	}
	/**
	 * 根据指定的格式转换java.sql.Timestamp到String
	 * 
	 * @param dt
	 *            java.sql.Timestamp instance
	 * @param sFmt
	 *            Date
	 *            格式，DATE_FORMAT_DATEONLY/DATE_FORMAT_DATETIME/DATE_FORMAT_SESSION
	 * @return
	 * @since 1.0
	 * @history
	 */
	public static String toSqlTimestampString(java.sql.Timestamp dt, String sFmt) {
		String temp = null;
		String out = null;
		if (dt == null || sFmt == null) {
			return null;
		}
		temp = dt.toString();
		if (sFmt.equals(DateUtils.DATE_FORMAT_DATETIME) || // "YYYY/MM/DD
				// HH24:MI:SS"
				sFmt.equals(DateUtils.DATE_FORMAT_DATEONLY)) { // YYYY/MM/DD
			temp = temp.substring(0, sFmt.length());
			out = temp.replace('/', '-');
			// }else if( sFmt.equals (DateUtils.DATE_FORMAT_SESSION ) ){
			// //Session
			// out =
			// temp.substring(0,4)+temp.substring(5,7)+temp.substring(8,10);
			// out += temp.substring(12,14) + temp.substring(15,17);
		}
		return out;
	}
	
	//得到当前日期的星期
	public static int getWeek(){
	    Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK);
		return w;
	}
	
	/**
	 * Timestamp 格式转换成yyyy-MM-dd
	 * timestampToSql(Timestamp 格式转换成yyyy-MM-dd)
	 * @param   timestamp 时间 
	 * @return createTimeStr  yyyy-MM-dd 时间 
	 * @Exception 异常对象 
	 * @since   V1.0
	 */
	public static String timestampToStringYMD(java.sql.Timestamp  timestamp){
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_DATEONLY);
		String createTimeStr = sdf.format(timestamp);
		return createTimeStr;
	}
	
	public static void main(String[] args) throws ParseException {
		Date d = parseDate("2010-12-19 14:16:50", "yyyy-MM-dd HH:mm:ss");
		System.out.println(d);
		System.out.println(toString(d, "yyyy/MM/dd"));
		Calendar c = Calendar.getInstance();
		System.out.println(c.isSet(Calendar.HOUR_OF_DAY) + "---" + c.getTime());
		
		System.out.println(compareDate(d, c.getTime(), Calendar.SECOND));
		
		Date startDate = parseDate("2012-12-19 14:16:50", "yyyy-MM-dd HH:mm:ss");
		System.out.println(isOverIntervalLimit(startDate, startDate, 10));
	}
	
	/**
	 * 判断一个时间是否在某个时间区间内
	 * 
	 * @param now
	 *            目标时间
	 * @param start
	 *            时间区间开始
	 * @param end
	 *            时间区间结束
	 * @param model
	 *            区间模式
	 * @return 是否在区间内
	 */
	public static boolean isBetween(Date now, Date start, Date end, int model) {

		if (now == null || start == null || end == null)
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		switch (model) {
		case LEFT_OPEN_RIGHT_OPEN: {
			if (now.after(start) && now.before(end))
				return true;
			break;
		}
		default: {
			return false;
		}
		}
		return false;
	}
	
	/**
	 * 得到当前周起始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getWeekStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.get(Calendar.WEEK_OF_YEAR);
		int firstDay = calendar.getFirstDayOfWeek();
		calendar.set(Calendar.DAY_OF_WEEK, firstDay);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 得到当月起始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 得到当前年起始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getYearStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}


	/**
	 * 取得月天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得月第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 取得月最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 取得季度第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getSeasonStart(Date date) {
		return getDayStart(getFirstDateOfMonth(getSeasonDate(date)[0]));
	}

	/**
	 * 取得季度最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getSeasonEnd(Date date) {
		return getDayStart(getLastDateOfMonth(getSeasonDate(date)[2]));
	}

	/**
	 * 取得季度月
	 * 
	 * @param date
	 * @return
	 */
	public static Date[] getSeasonDate(Date date) {
		Date[] season = new Date[3];

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int nSeason = getSeason(date);
		if (nSeason == 1) {// 第一季度
			c.set(Calendar.MONTH, Calendar.JANUARY);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.FEBRUARY);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.MARCH);
			season[2] = c.getTime();
		} else if (nSeason == 2) {// 第二季度
			c.set(Calendar.MONTH, Calendar.APRIL);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.MAY);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.JUNE);
			season[2] = c.getTime();
		} else if (nSeason == 3) {// 第三季度
			c.set(Calendar.MONTH, Calendar.JULY);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.AUGUST);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.SEPTEMBER);
			season[2] = c.getTime();
		} else if (nSeason == 4) {// 第四季度
			c.set(Calendar.MONTH, Calendar.OCTOBER);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.NOVEMBER);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.DECEMBER);
			season[2] = c.getTime();
		}
		return season;
	}

	/**
	 * 
	 * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
	 * 
	 * @param date
	 * @return
	 */
	public static int getSeason(Date date) {

		int season = 0;

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			season = 1;
			break;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			season = 2;
			break;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			season = 3;
			break;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			season = 4;
			break;
		default:
			break;
		}
		return season;
	}
	
	/**
	 *  判断开始时间和结束时间，是否超出了当前时间的一定的间隔数限制
	 *  如：开始时间和结束时间，不能超出距离当前时间90天
	 * @param startDate
	 * 			开始时间
	 * @param endDate
	 * 			结束时间按
	 * @param interval
	 * 			间隔数
	 * @param dateUnit
	 * 			单位(如：月，日),参照Calendar的时间单位
	 * @return
	 */
	public static boolean isOverIntervalLimit(Date startDate,Date endDate,int interval,int dateUnit){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(dateUnit, interval*(-1));
		Date curDate = getDayStart(cal.getTime());
		if(getDayStart(startDate).compareTo(curDate) < 0 || getDayStart(endDate).compareTo(curDate) < 0){
			return true;
		}
		return false;
	}
	
	/**
	 *  判断开始时间和结束时间，是否超出了当前时间的一定的间隔数限制, 时间单位默认为天数
	 *  如：开始时间和结束时间，不能超出距离当前时间90天
	 * 
	 * @param startDate
	 * 			开始时间
	 * @param endDate
	 * 			结束时间按
	 * @param interval
	 * 			间隔数
	 * @return
	 */
	public static boolean isOverIntervalLimit(Date startDate,Date endDate,int interval){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, interval*(-1));
		Date curDate = getDayStart(cal.getTime());
		if(getDayStart(startDate).compareTo(curDate) < 0 || getDayStart(endDate).compareTo(curDate) < 0){
			return true;
		}
		return false;
	}
	
	/**
	 *  判断开始时间和结束时间，是否超出了当前时间的一定的间隔数限制, 时间单位默认为天数
	 *  如：开始时间和结束时间，不能超出距离当前时间90天
	 * 
	 * @param startDate
	 * 			开始时间
	 * @param endDate
	 * 			结束时间按
	 * @param interval
	 * 			间隔数
	 * @return
	 */
	public static boolean isOverIntervalLimit(String startDateStr,String endDateStr,int interval){
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = DateUtils.parseDate(startDateStr, DateUtils.DATE_FORMAT_DATEONLY);
			endDate = DateUtils.parseDate(endDateStr, DateUtils.DATE_FORMAT_DATEONLY);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return isOverIntervalLimit(startDate, endDate, interval);
	}
	

}
