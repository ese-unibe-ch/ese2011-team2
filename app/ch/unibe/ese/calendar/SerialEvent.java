package ch.unibe.ese.calendar;

import java.util.Date;

public class SerialEvent extends CalendarEvent {

	protected EventSeries eventSeries;
	
	SerialEvent(Date start, Date end, String name, String visibility, 
			EventSeries eventSeries, EseCalendar calendar) {
		super(start, end, name, visibility, calendar);
		this.eventSeries = eventSeries;
		this.isASerie = true;
	}

}
