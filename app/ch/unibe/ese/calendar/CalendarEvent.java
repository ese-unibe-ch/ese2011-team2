package ch.unibe.ese.calendar;

import java.util.Date;

public class CalendarEvent extends CalendarEntry {
	
	protected boolean isASerie;
	
	public CalendarEvent(Date start, Date end, String name, String visibility, EseCalendar calendar) {
		super(start, end, name, visibility, calendar);
		isASerie = false;
	}
	public boolean isASerie(){
		return isASerie;
	}
	
}
