package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static int ONE_DAY_SECOND = 24 * 3600 * 1000;

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat myformatFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	final static private long SECOND_MS = 1000L;
	final static private long MINUTE_MS = 60 * 1000L;
	// final static private long DAY_MS = 24 * 60 * 60 * 1000L;
	final static public int DAY_S = 24 * 60 * 60;
	final static public int HOUR_S = 60 * 60;
	final static public int HALF_HOUR_S = 30 * 60;

	/**
	 * 计算当前日期与{@code endDate}的间隔天数
	 *
	 * @param endDate
	 * @return 间隔天数
	 */
	static long until(LocalDate endDate) {
		return LocalDate.now().until(endDate, ChronoUnit.DAYS);
	}

	/**
	 * 计算日期{@code startDate}与{@code endDate}的间隔天数
	 *
	 * @param startDate
	 * @param endDate
	 * @return 间隔天数
	 */
	static long until(LocalDate startDate, LocalDate endDate) {
		return startDate.until(endDate, ChronoUnit.DAYS);
	}

	/**
	 * 间隔天数
	 * 
	 * @param second1
	 * @param second2
	 * @return
	 */
	public static int until(long second1, long second2) {
		Date date1 = new Date(second1 * 1000);
		Instant instant1 = date1.toInstant();
		ZoneId zoneId1 = ZoneId.systemDefault();
		LocalDate localDate1 = instant1.atZone(zoneId1).toLocalDate();

		Date date2 = new Date(second2 * 1000);
		Instant instant2 = date2.toInstant();
		ZoneId zoneId2 = ZoneId.systemDefault();
		LocalDate localDate2 = instant2.atZone(zoneId2).toLocalDate();

		return (int) until(localDate1, localDate2);
	}

	static public int getToday() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String time = format.format(calendar.getTime());
		return Integer.valueOf(time);
	}

	static public int getDate(int deduct) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, deduct);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String time = format.format(calendar.getTime());
		return Integer.valueOf(time);
	}

	static public String getDate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(calendar.getTime());
	}

	static public int getSecondTime() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	static public int getMinuteTime() {
		return (int) (System.currentTimeMillis() / 1000 / 60);
	}

	static public Date parseDate(String dateString) {
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	static public Date parseDay(String dayString) {
		try {
			return dayFormat.parse(dayString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param YYYYMMDD
	 * @return
	 */
	public static int parseSecond(String dateString) {
		Date date = parseDate(dateString);
		return (int) (date.getTime() / 1000);
	}

	/**
	 * 
	 * @param yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static int parseDaySecond(String dayString) {
		Date date = parseDay(dayString);
		return (int) (date.getTime() / 1000);
	}

	public static int getDate(long second) {
		if (second == 0) {
			return 0;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(second * 1000);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String time = format.format(calendar.getTime());
		return Integer.valueOf(time);
	}

	public static Date parseDate(long second) {
		if (second == 0) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(second * 1000);
		return calendar.getTime();
	}

	public static String formatSecond(long second) {
		Date date = parseDate(second);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	public static int getHour() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MINUTE);
	}

	public static int getSecond() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.SECOND);
	}

	public static int getWeekDay() {
		Calendar calendar = Calendar.getInstance();
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDay == 0 ? 7 : weekDay;
	}

	public static String getTime(long second) {
		int hour = (int) (second / 3600);
		int minute = (int) (second % 3600) / 60;
		int s = (int) (second % 60);
		StringBuffer sb = new StringBuffer();
		if (hour > 0) {
			sb.append("hour").append(":").append(hour);
		}
		if (minute > 0) {
			sb.append("minute").append(":").append(minute);
		}
		if (s > 0) {
			sb.append("second").append(":").append(s);
		}
		return sb.toString();
	}

	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	public static int byteArrayToInt(byte[] bytes) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (bytes[i] & 0x000000FF) << shift;// 往高位游
		}
		return value;
	}

	public static byte[] convert(String str) {
		String sr[] = str.split(" ");
		byte[] bs = new byte[sr.length];
		for (int i = 0; i < sr.length; i++) {
			byte b = (byte) Integer.parseInt(sr[i], 16);
			bs[i] = b;
		}
		return bs;
	}

	public static void print(byte[] b) {
		for (byte bt : b) {
			System.out.print(bt + " ");
		}
		System.out.println();
	}

	public static int offset(int sourceDay, int offerDay) {
		Date sourceDate = parseDate(sourceDay + "");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sourceDate);
		calendar.add(Calendar.DAY_OF_MONTH, offerDay);
		return getDate(calendar.getTimeInMillis() / 1000);
	}

	/**
	 * YYYY-MM-DD
	 * 
	 * @param date
	 */
	public static int parseToInt(Date date) {
		try {
//			Date date = myformatFormat.parse(dstr);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String time = format.format(date.getTime());
			return Integer.valueOf(time);
		} catch (Exception e) {
			LogHelper.ERROR.error(e.getMessage(), e);
		}
		return getToday();
	}

	/**
	 * 第几天,同一天为第一天
	 * 
	 * @param origin
	 * @param now
	 * @return
	 */
	static public int dayiy(Date origin, Date now) {
		Calendar orignC = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		orignC.setTime(origin);
		orignC.set(Calendar.HOUR_OF_DAY, 0);
		orignC.set(Calendar.MINUTE, 0);
		orignC.set(Calendar.SECOND, 0);
		orignC.set(Calendar.MILLISECOND, 0);

		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return (int) ((calendar.getTimeInMillis() - orignC.getTimeInMillis()) / (24 * 3600 * 1000)) + 1;
	}

	public static int getCurrentSecond() {
		return (int) (System.currentTimeMillis() / SECOND_MS);
	}

	public static int getCurrentMinute() {
		return (int) (System.currentTimeMillis() / MINUTE_MS);
	}

	/*** 20200505 ***/
	public static int getCurrentDay() {
		Calendar c = Calendar.getInstance();
		int d = c.get(Calendar.YEAR) * 10000 + (c.get(Calendar.MONTH) + 1) * 100 + c.get(Calendar.DAY_OF_MONTH);
		return d;
	}

	public static int getCurrentWeek() {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		int d = c.get(Calendar.YEAR) * 100 + c.get(Calendar.WEEK_OF_YEAR);
		return d;
	}

	public static int getMonthAndDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int monthAndDay = (c.get(Calendar.MONTH) + 1) * 10000 + (c.get(Calendar.DAY_OF_MONTH)) * 100;
		return monthAndDay;
	}

	/**
	 * 获取该天的凌晨时刻（秒）
	 * 
	 * @param currentSecond
	 * @return
	 */
	public static int getTodayZone(int currentSecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currentSecond * 1000L);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return (int) (calendar.getTimeInMillis() / 1000);
	}
}
