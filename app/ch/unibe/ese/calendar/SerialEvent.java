package ch.unibe.ese.calendar;

import java.util.Date;

public class SerialEvent extends CalendarEventImpl {

	protected EventSeries eventSeries;
	
	SerialEvent(Date start, Date end, String name, Visibility visibility, 
			EventSeries eventSeries, EseCalendar calendar, String description) {
		super(start, end, name, visibility, calendar, description);
		this.eventSeries = eventSeries;
		this.isASerie = true;
	}

}
