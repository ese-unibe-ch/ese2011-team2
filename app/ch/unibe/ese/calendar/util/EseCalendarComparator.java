package ch.unibe.ese.calendar.util;

import java.util.Comparator;

import ch.unibe.ese.calendar.EseCalendar;

/**
 * The EseCalendarComparator will sort calendars alphabetically.
 */
public class EseCalendarComparator implements Comparator<EseCalendar> {

	@Override
	public int compare(EseCalendar cal1, EseCalendar cal2) {
		if (cal1.equals(cal2)) {
			return 0;
		}
		String cal1Name = cal1.getName();
		String cal2Name = cal2.getName();
		return cal1Name.compareTo(cal2Name);
	}

}
