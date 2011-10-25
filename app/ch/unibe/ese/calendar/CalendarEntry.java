package ch.unibe.ese.calendar;

import java.util.Date;

/**
 * Represents a thing contained in a calendar, i.e. an <code>Event</code> or an <code>EventSeries</code> 
 *
 */
public abstract class CalendarEntry {

	private boolean isPublic;
	private Date end;
	private Date start;
	private String name;

	CalendarEntry(Date start, Date end, String name, boolean isPublic) {
		if (name==null || start==null || end==null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.start = start;
		this.end = end;
		this.isPublic = isPublic;
	}

	/**
	 * 
	 * @return true iff this is a public event
	 */
	public boolean isPublic() {
		return isPublic;
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

	public void set(String eventName, Date startDate, Date endDate, boolean isPublic) {
		this.name = eventName;
		this.start = startDate;
		this.end = endDate;
		this.isPublic = isPublic;
	}

	@Override
	public String toString() {
		return "CalendarEvent [isPublic=" + isPublic + ", end=" + end
				+ ", start=" + start + ", name=" + name + "]";
	}

}