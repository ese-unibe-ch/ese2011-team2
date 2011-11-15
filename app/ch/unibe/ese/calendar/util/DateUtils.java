package ch.unibe.ese.calendar.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * A class providing utility methods to deal with {@link java.util.Date}s and {@link java.util.Calendar}
 *
 */
public class DateUtils {

	public static Date getStartOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		setToStartOfDay(calendar);
		return calendar.getTime();
	}
	
	public static void setToStartOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);	
	}

}
