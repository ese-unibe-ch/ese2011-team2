package ch.unibe.ese.calendar;

import java.util.Date;

import ch.unibe.ese.calendar.EventSeries.Repetition;

/**
 * Represents a thing contained in a calendar, i.e. a <code>CalendarEvent</code> or an <code>EventSeries</code> 
 *
 */
abstract class CalendarEntry {

	private Date end;
	private Date start;
	private String name;
	private EseCalendar calendar;
	String description;
	
	private Visibility visibility;
	
	CalendarEntry(Date start, Date end, String name, Visibility visibility, EseCalendar calendar, String description) {
		if (name==null || start==null || end==null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.start = start;
		this.end = end;
		this.calendar = calendar;
		this.description = description;
		this.visibility = visibility;
	}



	public Visibility getVisibility() {
		return visibility;
	}
	


	public Date getEnd() {
		return end;
	}


	public Date getStart() {
		return start;
	}


	public String getName() {
		return name;
	}
	

	public EseCalendar getCalendar() {
		return calendar;
	}
	

	public String getDescription(){
		return description;
	}

	@Override
	public String toString() {
		return "CalendarEvent [visibility=" + visibility + ", end=" + end
				+ ", start=" + start + ", name=" + name + "]";
	}


}