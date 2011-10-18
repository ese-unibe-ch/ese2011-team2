package ch.unibe.ese.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	public static Date parse(String dateString) throws ParseException {
		return dateFormat.parse(dateString);
	}
	
	public static String format(Date date) {
		return dateFormat.format(date);
	}
}
