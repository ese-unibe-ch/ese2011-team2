package ch.unibe.ese.calendar;

import java.util.Date;

public class CalendarEvent extends CalendarEntry {
	
	protected boolean isASerie;
	
	public CalendarEvent(Date start, Date end, String name, boolean isPublic, EseCalendar calendar) {
		super(start, end, name, isPublic, calendar);
		isASerie = false;
	}
	public boolean getIsASerie(){
		return isASerie;
	}
	
}
