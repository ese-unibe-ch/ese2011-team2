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

	public abstract Iterator<CalendarEvent> iterate(User user, Date start);

	public abstract CalendarEvent getEventById(User user, String id, Date start,
			boolean isSeries);

	public abstract CalendarEvent getEventByHash(User user, int hash, Date start);

	public abstract CalendarEvent removeEvent(User user, String id, Date start,
			boolean isSeries);

	public abstract EventSeries addEventSeries(User user, Date start,
			Date end, String eventName, Visibility visibility, Repetition repetition, String description);

	public abstract CalendarEvent addEvent(User user, Date start, Date end,
			String eventName, Visibility visibility, String description);

	public abstract User getOwner();

	public abstract String getName();

	/**
	 * Gets a list of all events starting within the 24 hour period starting at date;
	 * This returns normal as well as serial events for this day.
	 * 
	 * @param date the point in time specifying the start of the 24h period for which events are to be returned
	 * @return a list of the events
	 */
	public abstract SortedSet<CalendarEvent> getEventsAt(User user, Date dayStart);

}