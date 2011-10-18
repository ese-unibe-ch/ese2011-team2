package ch.unibe.ese.calendar;

import java.util.Date;

public class SerialEvent extends CalendarEvent {

	protected EventSeries eventSeries;
	
	SerialEvent(Date start, Date end, String name, boolean isPublic, EventSeries eventSeries) {
		super(start, end, name, isPublic);
		this.eventSeries = eventSeries;
	}

}
