package ch.unibe.ese.calendar;

import java.util.Date;

public class SerialEvent extends CalendarEvent {

	protected EventSeries eventSeries;
	
	SerialEvent(Date start, Date end, String name, boolean isPublic, 
			EventSeries eventSeries, EseCalendar calendar) {
		super(start, end, name, isPublic, calendar);
		this.eventSeries = eventSeries;
		this.isASerie = true;
	}

}
