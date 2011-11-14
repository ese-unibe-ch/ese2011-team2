package ch.unibe.ese.calendar;

import java.util.Date;


/**
 * Represent an event in a calendar. An event is a dedication  
 * of a single finite and continuous period of time.
 *
 */
public interface CalendarEvent {

	/**
	 * @see Visibility
	 * @return The visibility of the Event
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
	 * @return The name of the event
	 */
	public String getName();

	/**
	 * Every Event has a EseCalendar it belongs to. 
	 * This may not be null.
	 * @return The EseCalendar, which the entry belongs to.
	 */
	public EseCalendar getCalendar();
	
	/**
	 * If this event belongs to a series this returns this series.
	 * If not, null is returned.
	 * @return The EventSeries of this Event, null if this is an individual event
	 */
	public EventSeries getSeries();
	
	/**
	 * 
	 * @return a description of the event
	 */
	public String getDescription();

	/**
	 * For individual events the id contains no dash ('-') sign, for serial events
	 * if has exactly one dash so that the part before the dash is the id of the series
	 * and the part after the dash the consecutive number within the series.
	 * 
	 * @return the unique ID for this event.
	 */
	public String getId();
	
	/**
	 * 
	 * @param other
	 * @return true if this has the same ID as other, false otherwise
	 */
	public boolean equals(Object other); 
	
	/**
	 * 
	 * @return the hash of the id
	 */
	public int hashCode();

}