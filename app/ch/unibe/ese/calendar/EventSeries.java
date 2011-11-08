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
		DAILY("daily"), 
		/**
		 * weekly repetition
		 */
		WEEKLY("weekly"), 
		/**
		 * monthly repetition
		 */
		MONTHLY("monthly");
		
		private String name;

		private Repetition(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
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
	public String getId();
	
	/**
	 * @return the repetition of the events of this series
	 * @see Repetition
	 */
	public Repetition getRepetition();

	/**
	 * 
	 * This method allows iterating over the events that are instances of this series 
	 * sorted by date and including all instance ending at or after the time specified by the start argument
	 * 
	 * @param start the date from which on instances of the series are to be returned
	 * @return the iterator over the matchingCalendarEvents
	 */
	public Iterator<CalendarEvent> iterator(Date start);
	
	/**
	 * Get an event of this series by its consecutive number
	 * 
	 * @param consecutiveNumber the consecutive number of the requested event
	 * @return
	 */
	public CalendarEvent getEventByConsecutiveNumber(long consecutiveNumber);
	
	/**
	 * Add a exceptional instance, i.e. an event which replaces the normal occurence 
	 * of the event.
	 * 
	 * Note that at this occurence it will not be the passed instance for exceptionalEvent
	 * that is returned but an event with a regular serial-event id
	 * 
	 * @param id
	 * @param exceptionalEvent how the exceptional event should look like, or null if the instance is deleted from the series
	 */
	public void addExceptionalInstance(String id, CalendarEvent exceptionalEvent);

}