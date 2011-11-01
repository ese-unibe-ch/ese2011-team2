package ch.unibe.ese.calendar;

import java.util.Comparator;

class StartDateComparator implements Comparator<CalendarEvent> {

	@Override
	public int compare(CalendarEvent event1, CalendarEvent event2) {
		if (event1.equals(event2)) {
			return 0;
		}
		int dateComparison = event1.getStart().compareTo(event2.getStart());
		if (dateComparison == 0) {
			int endDateComparison = event1.getEnd().compareTo(event2.getEnd());
			if (endDateComparison == 0) {
				int stringComparison = event1.getName().compareTo(event2.getName());
				if (stringComparison == 0) {
					return event1.hashCode() - event2.hashCode();
				} else {
					return stringComparison;
				}
			} else {
				return endDateComparison;
			}
		} else {
			return dateComparison;
		}
	}

}
