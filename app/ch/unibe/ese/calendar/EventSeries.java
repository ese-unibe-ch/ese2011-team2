package ch.unibe.ese.calendar;

import java.util.Date;
import java.util.Iterator;

/**
 * A series of events
 *
 */
public interface EventSeries {
	
	/**
	 * Expresses the repetition mode
	 *
	 */
	public static enum Repetition {
		/**
		 * daily repetition
		 */
		DAILY, 
		/**
		 * weekly repetition
		 */
		WEEKLY, 
		/**
		 * monthly repetition
		 */
		MONTHLY
	}
	
	//TODO instead of having so many event properties it could have an event prototype
	/**
	 * @return the visibility of events of this series
	 */
	public Visibility getVisibility();

	/**
	 * 
	 * @return the end of evvent starting this series
	 */
	public Date getEnd();

	/**
	 * 
	 * @return the start of the event starting this series
	 */
	public Date getStart();

	/**
	 * The name is typically a short description of the event
	 * 
	 * @return the name of the event
	 */
	public String getName();

	/**
	 * Gets the calendar the series belongs to.
	 * @return a calendar, which the series belongs to.
	 */
	public EseCalendar getCalendar();
	
	/**
	 * 
	 * @return a description of the event
	 */
	public String getDescription();

	/**
	 * @return a unique ID for this series.
	 */
	public long getId();
	
	/**
	 * @return the repetionm of the events of this series
	 */
	public Repetition getRepetition();

	/**
	 * 
	 * @param start the date from which on instances of the series are to be returned
	 * @return an iterator over the event that are instances of this series
	 */
	public Iterator<CalendarEvent> iterator(Date start);

}