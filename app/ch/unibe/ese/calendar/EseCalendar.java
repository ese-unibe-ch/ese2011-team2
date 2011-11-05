package ch.unibe.ese.calendar;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.unibe.ese.calendar.EventSeries.Repetition;

public abstract class EseCalendar {

	public EseCalendar() {
		super();
	}

	/**
	 * Iterates through all events with a start date after start
	 * 
	 * @param start the date at which to start iterating events
	 * @return an iterator with events starting after start
	 */
	public abstract Iterator<CalendarEvent> iterate(User user, Date start);

	/**
	 * If the id belongs to a EventSeries, this method returns the prototype.
	 * Beware, that the prototype could also be an exceptionalInstance.
	 * 
	 * @param user the user requesting the operation
	 * @param id the id of the event
	 * @return the event by that id
	 */
	public abstract CalendarEvent getEventById(User user, String id);


	/**
	 * Removes an event from the calendar
	 * 
	 * @return the event removed
	 */
	public abstract CalendarEvent removeEvent(User user, String id);
	
	/**
	 * Removes an event series from the calendar
	 * 
	 * @return the event series removed
	 */
	public abstract EventSeries removeEventSeries(User user, String id);

	/**
	 * Adds an event series to this calendar
	 * 
	 * @param user the user requesting the operation
	 * @param start the start of the prototype event
	 * @param end the end of the prototype event
	 * @param eventName the name of the event
	 * @param visibility the visibility of events belonging to this series
	 * @param repetition how often events shall repeat
	 * @param description a description of events in this series
	 * @return the new EventSeries
	 */
	public abstract EventSeries addEventSeries(User user, Date start,
			Date end, String eventName, Visibility visibility, Repetition repetition, String description);

	/**
	 * Adds the event to the calendar
	 * 
	 */
	public abstract CalendarEvent addEvent(User user, Date start, Date end,
			String eventName, Visibility visibility, String description);

	/**
	 * When <code>ch.unibe.ese.calendar.security.CalendarPolicy</code> is in place the owner can write to this calendar.
	 * 
	 * @return the owner of this calendar
	 */
	public abstract User getOwner();

	/**
	 * 
	 * @return the name of this calendar
	 */
	public abstract String getName();

	/**
	 * Gets a unmodifiable SortedSet of all events starting within the 24 hour period starting at date;
	 * This returns normal as well as serial events for this day.
	 * 
	 * @param date the point in time specifying the start of the 24h period for which events are to be returned
	 * @return a list of the events
	 */
	public abstract SortedSet<CalendarEvent> getEventsAt(User user, Date dayStart);

}