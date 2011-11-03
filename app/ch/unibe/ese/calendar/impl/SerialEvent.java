package ch.unibe.ese.calendar.impl;

import java.util.Date;

import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.Visibility;

class SerialEvent extends CalendarEventImpl {

	private EventSeries eventSeries;
	private long consecutiveNumber;
	
	SerialEvent(Date start, Date end, String name, Visibility visibility, 
			EventSeries eventSeries, EseCalendar calendar, String description, long consecutiveNumber) {
		super(start, end, name, visibility, calendar, description);
		this.eventSeries = eventSeries;
		this.consecutiveNumber = consecutiveNumber;
	}

	@Override
	public EventSeries getSeries() {
		return eventSeries;
	}
	
	@Override
	public String getId() {
		return eventSeries.getId()+"-"+consecutiveNumber;
	}
	

}
