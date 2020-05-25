package util;

import java.util.Calendar;
import java.util.Date;

public class WarHelper {

	final static private long SECOND_MS = 1000L;
	final static private long MINUTE_MS = 60 * 1000L;
	// final static private long DAY_MS = 24 * 60 * 60 * 1000L;
	final static public int DAY_S = 24 * 60 * 60;
	final static public int HOUR_S = 60 * 60;
	final static public int HALF_HOUR_S = 30 * 60;
	static Date OPEN_DATE = new Date();

	public static boolean isWarBeginReg() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (hour == 19 && minute == 30) {
			return true;
		}

		// if (hour == 15 && minute == 0) {
		// return true;
		// }

		return false;
	}

	public static boolean isWarBeginEnd() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (hour == 19 && minute == 55) {
			return true;
		}

		// if (hour == 15 && minute == 30) {
		// return true;
		// }

		return false;
	}

	public static boolean isWarBeginFight() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (hour == 20 && minute == 0) {
			return true;
		}

		// if (hour == 16 && minute == 0) {
		// return true;
		// }

		return false;
	}

	public static boolean isWarDay() {
		int day = DateUtil.dayiy(OPEN_DATE, new Date());
		if (day <= 7) {
			return false;
		}

		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.WEDNESDAY || dayOfWeek == Calendar.SATURDAY) {
			return true;
		}

		return false;

		// return true;
	}

	public static boolean isWarOpen() {
		int day = DateUtil.dayiy(OPEN_DATE, new Date());
		if (day <= 7) {
			return false;
		}

		return true;
	}

	public static boolean isBossBegin() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (hour == 20 && minute == 50) {
			return true;
		}

		return false;
	}

	public static boolean isBossFightBegin() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (hour == 21 && minute == 0) {
			return true;
		}

		return false;
	}

	public static boolean isBossFightEnd() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (hour == 21 && minute == 30) {
			return true;
		}

		return false;
	}

	public static boolean isBossDay() {
		int day = DateUtil.dayiy(OPEN_DATE, new Date());
		if (day <= 30) {
			return false;
		}

		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.FRIDAY) {
			return true;
		}

		return false;
	}

	public static boolean isBossOpen() {
		int day = DateUtil.dayiy(OPEN_DATE, new Date());
		if (day <= 30) {
			return false;
		}

		return true;
	}

	public static boolean isStaffingOpen() {
		int day = DateUtil.dayiy(OPEN_DATE, new Date());
		if (day <= 30) {
			return false;
		}

		return true;
	}
}
