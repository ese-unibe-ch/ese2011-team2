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


	/* (non-Javadoc)
	 * @see ch.unibe.ese.calendar.TempInt#getVisibility()
	 */
	public Visibility getVisibility() {
		return visibility;
	}
	
	/* (non-Javadoc)
	 * @see ch.unibe.ese.calendar.TempInt#hasType(java.lang.String)
	 */
	public boolean hasType(String type){
		return visibility.toString().equalsIgnoreCase(type);
	}

	/* (non-Javadoc)
	 * @see ch.unibe.ese.calendar.TempInt#getEnd()
	 */
	public Date getEnd() {
		return end;
	}

	/* (non-Javadoc)
	 * @see ch.unibe.ese.calendar.TempInt#getStart()
	 */
	public Date getStart() {
		return start;
	}

	/* (non-Javadoc)
	 * @see ch.unibe.ese.calendar.TempInt#getName()
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see ch.unibe.ese.calendar.TempInt#getCalendar()
	 */
	public EseCalendar getCalendar() {
		return calendar;
	}
	
	/* (non-Javadoc)
	 * @see ch.unibe.ese.calendar.TempInt#getDescription()
	 */
	public String getDescription(){
		return description;
	}
	/* (non-Javadoc)
	 * @see ch.unibe.ese.calendar.TempInt#toString()
	 */
	@Override
	public String toString() {
		return "CalendarEvent [visibility=" + visibility + ", end=" + end
				+ ", start=" + start + ", name=" + name + "]";
	}
	
	/* (non-Javadoc)
	 * @see ch.unibe.ese.calendar.TempInt#isASerie()
	 */
	public abstract boolean isASerie();

}