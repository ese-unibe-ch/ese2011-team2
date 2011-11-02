package ch.unibe.ese.calendar;

import java.util.Date;


public interface CalendarEvent {

	/**
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
	
	/**
	 * if this event belongs to a series this returns this series
	 * @return the EventSeries of null if this is an individual event
	 */
	public EventSeries getSeries();
	/**
	 * 
	 * @return a description of the event
	 */
	public String getDescription();

	/**
	 * TODO: Dummy events for comparison get also an ID. This could be harmful.
	 * A CalendarEntry can not have the same ID as a EventSeries.
	 * Every SerialEvent has the ID of the EventSeries it was created by.
	 * @return a unique ID for every event.
	 */
	public long getId();

}