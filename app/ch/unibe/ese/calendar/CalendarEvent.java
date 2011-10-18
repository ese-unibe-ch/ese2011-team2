package ch.unibe.ese.calendar;

import java.util.Date;

public class CalendarEvent extends CalendarEntry {

	public CalendarEvent(Date start, Date end, String name, boolean isPublic) {
		super(start, end, name, isPublic);
	}
	
}
