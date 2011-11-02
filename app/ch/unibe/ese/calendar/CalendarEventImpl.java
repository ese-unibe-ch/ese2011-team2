package ch.unibe.ese.calendar;

import java.util.Date;

class CalendarEventImpl extends CalendarEntry implements CalendarEvent {
	
	protected boolean isASerie;
	
	CalendarEventImpl(Date start, Date end, String name, Visibility visibility, EseCalendar calendar, String description) {
		super(start, end, name, visibility, calendar, description);
		isASerie = false;
		
	}
	/* (non-Javadoc)
	 * @see ch.unibe.ese.calendar.CalendarEvent#isASerie()
	 */
	public boolean isASerie(){
		return isASerie;
	}
	
}
