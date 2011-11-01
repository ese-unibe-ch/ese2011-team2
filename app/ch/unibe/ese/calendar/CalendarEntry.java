package ch.unibe.ese.calendar;

import java.util.Date;

import ch.unibe.ese.calendar.EventSeries.Repetition;

/**
 * Represents a thing contained in a calendar, i.e. an <code>Event</code> or an <code>EventSeries</code> 
 *
 */
public abstract class CalendarEntry {

	private Date end;
	private Date start;
	private String name;
	private EseCalendar calendar;
	String description;
	
	public enum Visibility {
		PRIVATE,PUBLIC,BUSY
	}

	private Visibility visibility;
	
	CalendarEntry(Date start, Date end, String name, String visibility, EseCalendar calendar, String description) {
		if (name==null || start==null || end==null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.start = start;
		this.end = end;
		this.calendar = calendar;
		this.description = description;
		setVisibility(visibility);
		}

		/* (non-Javadoc)
		 * @see ch.unibe.ese.calendar.TempInt#setVisibility(java.lang.String)
		 */
		public void setVisibility(String visibility) {
			if (visibility.equalsIgnoreCase("PRIVATE"))
				this.visibility = Visibility.PRIVATE;
			if (visibility.equalsIgnoreCase("PUBLIC"))
				this.visibility = Visibility.PUBLIC;
			if (visibility.equalsIgnoreCase("BUSY"))
				this.visibility = Visibility.BUSY;
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