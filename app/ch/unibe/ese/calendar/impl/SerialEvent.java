package ch.unibe.ese.calendar.impl;

import java.util.Date;

import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.Visibility;

class SerialEvent extends CalendarEventImpl {

	protected EventSeries eventSeries;
	
	SerialEvent(Date start, Date end, String name, Visibility visibility, 
			EventSeries eventSeries, EseCalendar calendar, String description) {
		super(start, end, name, visibility, calendar, description);
		this.eventSeries = eventSeries;
	}

	@Override
	public EventSeries getSeries() {
		return eventSeries;
	}
	
	@Override
	public long getId() {
		return eventSeries.getId();
	}
	

}
