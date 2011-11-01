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

		public void setVisibility(String visibility) {
			if (visibility.equalsIgnoreCase("PRIVATE"))
				this.visibility = Visibility.PRIVATE;
			if (visibility.equalsIgnoreCase("PUBLIC"))
				this.visibility = Visibility.PUBLIC;
			if (visibility.equalsIgnoreCase("BUSY"))
				this.visibility = Visibility.BUSY;
		}

	/**
	 * 
	 * @return true if this is a public event
	 */
	public Visibility getVisibility() {
		return visibility;
	}
	
	public boolean hasType(String type){
		return visibility.toString().equalsIgnoreCase(type);
	}

	/**
	 * 
	 * @return the end of event
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * 
	 * @return the start of the event
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * The name is typically a short description of the event
	 * 
	 * @return the name of the event
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the calendar the entry belongs to.
	 * @return a calendar, which the entry belongs to.
	 */
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
	
	public abstract boolean isASerie();

}