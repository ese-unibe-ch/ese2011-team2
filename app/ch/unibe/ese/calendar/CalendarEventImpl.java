package ch.unibe.ese.calendar;

import java.util.Date;

class CalendarEventImpl extends CalendarEntry implements CalendarEvent {
	
	
	CalendarEventImpl(Date start, Date end, String name, Visibility visibility, EseCalendar calendar, String description) {
		super(start, end, name, visibility, calendar, description);
		
	}

	@Override
	public EventSeries getSeries() {
		return null;
	}
	
}
