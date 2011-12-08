package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
	private static SimpleDateFormat date10 = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat date08 = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat time08 = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat time06 = new SimpleDateFormat("HHmmss");
	private static SimpleDateFormat time07 = new SimpleDateFormat("mmssSSS");

	public static String getToday10() {
		return date10.format(new Date());
	}

	public static String getToday08() {
		return date08.format(new Date());
	}
	
	public static String getTime08() {
		return time08.format(new Date());
	}

	public static String getTime06() {
		return time06.format(new Date());
	}

	public static String getTime07() {
		return time07.format(new Date());
	}
}