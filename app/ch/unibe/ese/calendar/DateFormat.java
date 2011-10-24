package ch.unibe.ese.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	/**
	 * @param dateString String with format "dd.MM.yyyy HH:mm"
	 * @return A Date object with the given arguments
	 * @throws ParseException
	 */
	public static Date parse(String dateString) throws ParseException {
		return dateFormat.parse(dateString);
	}
	
	/**
	 * @param date
	 * @return A String out of the given Date formated as "dd.MM.yyyy HH:mm"
	 */
	public static String format(Date date) {
		return dateFormat.format(date);
	}
}
