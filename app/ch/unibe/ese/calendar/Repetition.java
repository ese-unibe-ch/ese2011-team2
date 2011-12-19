package ch.unibe.ese.calendar;

import java.util.Calendar;

/**
 * Expresses the interval between two instances of a EventSeries.
 *
 */
public enum Repetition {
	/**
	 * Daily repetition, meaning every 1 day
	 */
	DAILY(Calendar.DAY_OF_MONTH), 
	/**
	 * Weekly repetition, meaning every 7 day
	 */
	WEEKLY(Calendar.WEEK_OF_YEAR), 
	/**
	 * Monthly repetition, meaning every n'th day of the month, if the month 
	 * doesn't has that day the instance occurs earlier as to be occur in each 
	 * calendar month.
	 * NOT every 30 days or something of the like.
	 */
	MONTHLY(Calendar.MONTH);
	
	private int calendarField;

	private Repetition(int calendarField) {
		this.calendarField = calendarField;
	}
	
	public int getCalendarField() {
		return calendarField;
	}
}
