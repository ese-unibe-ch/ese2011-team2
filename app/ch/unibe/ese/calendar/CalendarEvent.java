package ch.unibe.ese.calendar;

import java.util.Date;


public interface CalendarEvent {

	/**
	 * 
	 * @return true if this is a public event
	 */
	public Visibility getVisibility();

	/**
	 * 
	 * @return the end of event
	 */
	public Date getEnd();

	/**
	 * 
	 * @return the start of the event
	 */
	public Date getStart();

	/**
	 * The name is typically a short description of the event
	 * 
	 * @return the name of the event
	 */
	public String getName();

	/**
	 * Gets the calendar the entry belongs to.
	 * @return a calendar, which the entry belongs to.
	 */
	public EseCalendar getCalendar();

	public String getDescription();

	public String toString();

	public boolean isASerie();

}