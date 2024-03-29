package ch.unibe.ese.calendar;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;

import ch.unibe.ese.calendar.exceptions.EventNotFoundException;
import ch.unibe.ese.calendar.security.PermissionDeniedException;

/**
 * Represents a calendar, a calendar is collection of (individual) events
 * and series of events.
 *
 */
public abstract class EseCalendar {


	/**
	 * Iterates through all events with an end date after a specified date
	 * 
	 * @param earliestEndDate the date at which to start iterating events
	 * @return an iterator with events starting after earliestEndDate
	 */
	public abstract Iterator<CalendarEvent> iterate(User user, Date earliestEndDate);

	/**
	 * Returns the Event to the given id.
	 * If the id belongs to a SerialEvent, it returns the exact instance
	 * of this series.
	 * The returned Event could also be an exceptionalInstance with other properties
	 * than the usual SerialEvent created by the EventSeries.
	 * </br>
	 * This doesn't work with ids of EventSeries, only for ids of 
	 * SerialEvents created by a EventSeries.
	 * 
	 * @param user the user requesting the operation
	 * @param id the id of the event
	 * @return the event by that id
	 * @throws EventNotFoundException 
	 * 		if there is no event with this id
	 * @throws PermissionDeniedException 
	 * 		if the user doesn't have the permission to see this event.
	 */
	public abstract CalendarEvent getEventById(User user, String id);


	/**
	 * Removes an event from the calendar
	 * 
	 * @param user The user attempting to remove an event
	 * @param id The id of the event that is to be removed
	 * @return the event removed
	 * @throws EventNotFoundException 
	 * 		if there is no event with this id
	 * @throws PermissionDeniedException 
	 * 		if the user doesn't have the permission to remove this event.
	 */
	public abstract CalendarEvent removeEvent(User user, String id);
	
	/**
	 * Removes an EventSeries from the calendar.
	 * Careful, the id has to be from a SerialEvent created by the EventSeries,
	 * not from the EventSeries itself.
	 * 
	 * @param user The user attempting to remove an EventSeries
	 * @param id The id a SerialEvent that belongs to the Series that is to be removed
	 * @return the event series removed
	 * @throws EventNotFoundException 
	 * 		if there is no event with this id
	 * @throws PermissionDeniedException 
	 * 		if the user doesn't have the permission to see this event.
	 */
	public abstract EventSeries removeEventSeries(User user, String id);

	/**
	 * Adds an EventSeries to this calendar
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
	 * Adds an event to the calendar
	 * 
	 * @param user the user requesting the operation
	 * @param start the start of the event
	 * @param end the end of the event
	 * @param eventName the name of the event
	 * @param visibility the visibility of events belonging to this series
	 * @param description a description of events in this series
	 * @return the created CalendarEvent
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
	 * Gets a unmodifiable SortedSet of all events starting before the end of the specified day
	 * and ending after the start of the specified day, i.e. all events that have an intersection 
	 * with the specified day.
	 * 
	 * This returns normal as well as serial events for this day.
	 * 
	 * @param day a point in time belonging to the day for which the events are requested
	 * @return a list of the events
	 */
	public abstract SortedSet<CalendarEvent> getEventsAt(User user, Date day);
	public abstract SortedSet<CalendarEvent> getEventsByRegex(User user, String regex);
	
	/** 
	 * sets the argument isSelected to select
	 * 
	 * @param select: true if this calendar should be displayed in the view
	 * @param select: false if not
	 */
	public abstract void select(boolean select);
	
	/** 
	 * Indicates whether this calendar should be displayed in the view
	 * 
	 */
	public abstract boolean isSelected();
	

}
