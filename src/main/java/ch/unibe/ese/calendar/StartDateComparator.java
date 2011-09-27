package ch.unibe.ese.calendar;

import java.util.Comparator;

class StartDateComparator implements Comparator<CalendarEvent> {

	@Override
	public int compare(CalendarEvent event1, CalendarEvent event2) {
		return event1.getStart().compareTo(event2.getStart());
	}

}
