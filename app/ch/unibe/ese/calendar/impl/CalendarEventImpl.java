package ch.unibe.ese.calendar.impl;

import java.util.Date;

import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.Visibility;

class CalendarEventImpl extends CalendarEntry implements CalendarEvent {
	
	
	CalendarEventImpl(Date start, Date end, String name, Visibility visibility, EseCalendar calendar, String description) {
		super(start, end, name, visibility, calendar, description);
	}

	@Override
	public EventSeries getSeries() {
		return null;
	}
	
}
