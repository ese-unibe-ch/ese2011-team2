package ch.unibe.ese.calendar;

import java.util.Date;
import java.util.Iterator;


/**
 * A series of events. Additionally to a CalendarEvent, an EventSeries has also an attribute 
 * called repetition, that defines in which interval it is repeated.
 *
 */
public interface EventSeries {
	
	/**
	 * Expresses the interval between two instances of a EventSeries.
	 *
	 */
	public static enum Repetition {
		/**
		 * Daily repetition, meaning every 1 day
		 */
		DAILY("daily"), 
		/**
		 * Weekly repetition, meaning every 7 day
		 */
		WEEKLY("weekly"), 
		/**
		 * Monthly repetition, meaning every n'th day of the month.
		 * NOT every 30 days or something of the like.
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
	 * @see Visibility
	 * @return The visibility of this EventSeries.
	 */
	public Visibility getVisibility();

	/**
	 * 
	 * @return The end of the prototype defining this EventSeries.
	 */
	public Date getEnd();

	/**
	 * 
	 * @return The start of prototype defining this EventSeries.
	 */
	public Date getStart();

	/**
	 * The name is typically a short description of the EventSeries
	 * 
	 * @return The name of the EventSeries
	 */
	public String getName();

	/**
	 * @return The EseCalendar the EventSeries belongs to.
	 */
	public EseCalendar getCalendar();
	
	/**
	 * Everything that is too large for being a name, should be a
	 * description.
	 * 
	 * @return A description (String) of the EventSeries.
	 */
	public String getDescription();

	/**
	 * @return A unique ID for this EventSeries.
	 */
	public String getId();
	
	/**
	 * @return the repetition interval of this EventSeries
	 * @see Repetition
	 */
	public Repetition getRepetition();

	/**
	 * 
	 * This method allows iterating over the events that are instances of this series 
	 * sorted by date and including all instance ending at or after the time specified by the earliestEndDate argument
	 * 
	 * @param earliestEndDate the date from which on instances of the series are to be returned
	 * @return the iterator over the matchingCalendarEvents
	 */
	public Iterator<CalendarEvent> iterator(Date earliestEndDate);
	
	/**
	 * Get an CalendarEvent of this series by its consecutive number. The prototype
	 * of the EventSeries has the consecutiveNumber 0. CalendarEvents in after the prototype
	 * have positive, before the prototype negative values.
	 * 
	 * @param consecutiveNumber the consecutive number of the requested event
	 * @return The CalendarEvent with the given consecutive number.
	 */
	public CalendarEvent getEventByConsecutiveNumber(long consecutiveNumber);
	
	/**
	 * Add a exceptional instance, i.e. an event which replaces the normal occurrence 
	 * of the EventSeries.
	 * 
	 * @param id The id of the occurrence that is to be replaced.
	 * @param exceptionalEvent A CalendarEvent with other attributes than a normal instance of the series
	 *    or null if the instance is to be deleted from the series
	 */
	public void addExceptionalInstance(String id, CalendarEvent exceptionalEvent);

}