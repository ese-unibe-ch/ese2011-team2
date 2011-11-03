package ch.unibe.ese.calendar.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EseDateFormat {
	private static EseDateFormat instance;
	private DateFormat dateFormat;

	private EseDateFormat() {
		dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		dateFormat.setLenient(false);
	}
	
	public static EseDateFormat getInstance() {
		if (instance == null) {
			instance = new EseDateFormat();
		}
		return instance;
	}
	
	/**
	 * @param dateString String with format "dd.MM.yyyy HH:mm"
	 * @return A Date object with the given arguments
	 * @throws ParseException 
	 */
	public Date parse(String dateString) throws ParseException {
		return dateFormat.parse(dateString);
	}
	
	/**
	 * @param date
	 * @return A String out of the given Date formated as "dd.MM.yyyy HH:mm"
	 */
	public String format(Date date) {
		return dateFormat.format(date);
	}
}
