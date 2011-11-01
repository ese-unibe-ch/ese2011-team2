package ch.unibe.ese.calendar;

import java.security.AccessControlException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.unibe.ese.calendar.EventSeries.Repetition;
import ch.unibe.ese.calendar.exceptions.EventNotFoundException;
import ch.unibe.ese.calendar.security.Policy;
import ch.unibe.ese.calendar.security.PrivilegedCalendarAccessPermission;

/**
 * 
 * A Calendar is a collections of individual events and series of events It provides methods to access 
 * the events at a certain day or to iterate through them. Most methods receive the user for which the 
 * operation is performed the operation is then executed (or denied execution) according the 
 * permissions of the user.
 *
 */
public class EseCalendar {
	
	private SortedSet<CalendarEvent> startDateSortedSet = new TreeSet<CalendarEvent>(new StartDateComparator());
	//what's the point on sorting them by start date, it could be just a set no?
	private SortedSet<EventSeries> startDateSortedSetOfSeries = new TreeSet<EventSeries>(new StartDateComparator());
	/**
	 * Constructs a calendar. Application typically create and retrieve calendars using the CalendarManager.
	 * 
	 * @param name the name of the calendar
	 * @param owner the owner of the calendar
	 */
	protected EseCalendar(String name, User owner) {
		this.name = name;
		this.owner = owner;
	}

	private String name;
	private User owner;
	
	/**
	 * 
	 * @return the name of this calendar
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * When <code>ch.unibe.ese.calendar.security.CalendarPolicy</code> is in place the owner can write to this calendar.
	 * 
	 * @return the owner of this calendar
	 */
	public User getOwner() {
		return owner;
	}
	
	/**
	 * this method is used only for tests.  
	 */
	//I think we should use blackbox tests instead (reto)
	SortedSet<CalendarEvent> getStartDateSortedSet() {
		return startDateSortedSet;
	}
	
	/**
	 * this method is used only for tests. I think we should use blackbox tests instead 
	 */
	//I think we should use blackbox tests instead (reto)
	SortedSet<EventSeries> getStartDateSortedSetOfSeries() {
		return startDateSortedSetOfSeries;
	}

	/**
	 * Adds the event to the calendar
	 * 
	 * @param calendarEvent
	 */
	public CalendarEvent addEvent(User user, Date start, Date end, String eventName, String visibility, String description) {
		Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		CalendarEvent event = new CalendarEvent(start, end, eventName, visibility, this, description);
		startDateSortedSet.add(event);
		return event;
	}
	
	public EventSeries addEventSeries(User user, Date start, Date end, String eventName, String visibility, 
			String sRepetition, String description){
		Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		EventSeries eventSeries = new EventSeries(start, end, eventName, visibility, sRepetition, this, description);
		startDateSortedSetOfSeries.add(eventSeries);
		return eventSeries;
	}
	
	/**
	 * Removes an event from the calendar
	 * 
	 * Needs a startDate so we don't have to go through the whole list for finding the right event.
	 * @return the event removed
	 */
	public CalendarEntry removeEvent(User user, int hash, Date start) {
		Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		CalendarEntry e = getEventByHash(user, hash, start);
		startDateSortedSet.remove(e);
		return e;
	}
	
	/**
	 * Only returns an event if the user has privileged access.
	 * @param hash The hash the event produces by calling hashCode()
	 * @return null, if the Event is not found.
	 */
	//TODO use a uid instead of hash
	public CalendarEntry getEventByHash(User user, int hash, Date start) {
		Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		Iterator<CalendarEvent> afterStart = iterate(user, start);
		//TODO: also check, startDateSortedSet for this hash (or create own method for deleting series)
		CalendarEntry e;
		do {
			e = afterStart.next();
		} while (e != null && e.hashCode() != hash);
		if (e == null)
			throw new EventNotFoundException("Permission denied");
		return e;
	}

	/**
	 * Iterates through all events with a start date after start
	 * 
	 * @param start the date at which to start iterating events
	 * @return an iterator with events starting after start
	 */
	public Iterator<CalendarEvent> iterate(User user, Date start) {
		Iterator<CalendarEvent> iterateIndividual = iterateIndividualEvents(user, start);
		Iterator<CalendarEvent> iterateSeries = iterateSerialEvents(user, start);
		return new EventIteratorMerger(iterateIndividual, iterateSeries);
	}
	
	/**
	 * Iterates through the non-serial events with a start date after start
	 * 
	 * @param start the date at which to start iterating events
	 * @return an iterator with events starting after start
	 */
	Iterator<CalendarEvent> iterateIndividualEvents(User user, Date start) {
		CalendarEvent compareDummy = new CalendarEvent(start, start, "compare-dummy", "Private", this, "");
		Iterator<CalendarEvent> unfilteredEvents = startDateSortedSet.tailSet(compareDummy).iterator();
		return new ACFilteringEventIterator(user, unfilteredEvents);
	}
	
	/**
	 * Iterates through the serial events with a start date after start
	 * 
	 * @param start the date at which to start iterating events
	 * @return an iterator with events starting after start
	 */
	Iterator<CalendarEvent> iterateSerialEvents(User user, Date start) {
		List<Iterator<CalendarEvent>> seriesIterators = new ArrayList<Iterator<CalendarEvent>>(); 
		Iterator<EventSeries> eventSeries = iterateSeries(user);
		while (eventSeries.hasNext()) {
			EventSeries series = eventSeries.next();
			seriesIterators.add(series.iterator(start));
		}
		if (seriesIterators.size() > 1) {
			return new EventIteratorMerger(seriesIterators);
		}
		if (seriesIterators.size() == 1) {
			return seriesIterators.get(0);
		}
		//empty iterator
		return Collections.EMPTY_LIST.iterator();
	}
	
	
	/**
	 * Iterates through all serial events
	 * 
	 * @return an iterator with all serial events
	 */
	Iterator<EventSeries> iterateSeries(User user){
		Iterator<EventSeries> allEventSeries = startDateSortedSetOfSeries.iterator();
		return new ACFilteringEventSeriesIterator(user, allEventSeries);
	}
	
	/**
	 * Gets a list of all events starting within the 24 hour period starting at date;
	 * 
	 * @param date the point in time specifying the start of the 24h period for which events are to be returned
	 * @return a list of the events
	 */
	public SortedSet<CalendarEvent> getEventsAt(User user, Date date) {
		Date endDate = new Date(date.getTime()+24*60*60*1000);
		SortedSet<CalendarEvent> result = new TreeSet<CalendarEvent>(new StartDateComparator());
		Iterator<CalendarEvent> iter = iterate(user, date);
		while (iter.hasNext()) {
			CalendarEvent ce = iter.next();
			if (ce.getStart().compareTo(endDate) > 0) {
				break;
			}
			result.add(ce);
		}
		return result;
	}
	
	
	
	

	private class ACFilteringEventIterator implements Iterator<CalendarEvent> {

		private boolean hasNext;
		private CalendarEvent next;
		private Iterator<CalendarEvent> unfilteredEvents;
		private User user;
		
		public ACFilteringEventIterator(User user, Iterator<CalendarEvent> unfilteredEvents) {
			this.unfilteredEvents = unfilteredEvents;
			this.user = user;
			prepareNext();
		}

		private void prepareNext() {
			hasNext = false;
			if (unfilteredEvents.hasNext()) {
				CalendarEvent ce = unfilteredEvents.next();
				if (ce.hasType("Private")) {
					if (!Policy.getInstance().hasPermission(user, new PrivilegedCalendarAccessPermission(name))) {
						prepareNext();
						return;
					}
				}
				next = ce;
				hasNext = true;
			}
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public CalendarEvent next() {
			if (!hasNext) {
				throw new NoSuchElementException();
			}
			CalendarEvent result = next;
			prepareNext();
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("not supported yet");
		}

	}
	private class ACFilteringEventSeriesIterator implements Iterator<EventSeries> {

		private boolean hasNext;
		private EventSeries next;
		private Iterator<EventSeries> eventSeries;
		private User user;
		
		public ACFilteringEventSeriesIterator(User user, Iterator<EventSeries> eventSeries) {
			this.eventSeries = eventSeries;
			this.user = user;
			prepareNext();
		}

		private void prepareNext() {
			hasNext = false;
			if (eventSeries.hasNext()) {
				EventSeries es = eventSeries.next();
				if (es.hasType("Private")) {
					if (!Policy.getInstance().hasPermission(user, new PrivilegedCalendarAccessPermission(name))) {
						prepareNext();
						return;
					}
				}
				next = es;
				hasNext = true;
			}
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public EventSeries next() {
			if (!hasNext) {
				throw new NoSuchElementException();
			}
			EventSeries result = next;
			prepareNext();
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("not supported yet");
		}

	}

	

}
