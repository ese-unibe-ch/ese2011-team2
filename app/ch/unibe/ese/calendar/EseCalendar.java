package ch.unibe.ese.calendar;

import java.security.AccessControlException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.unibe.ese.calendar.EventSeries.Repetition;
import ch.unibe.ese.calendar.security.Policy;
import ch.unibe.ese.calendar.security.PrivilegedCalendarAccessPermission;

public class EseCalendar {
	
	private SortedSet<CalendarEvent> startDateSortedSet = new TreeSet<CalendarEvent>(new StartDateComparator());
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
	 * Adds the event to the calendar
	 * 
	 * @param calendarEvent
	 */
	public void addEvent(User user, CalendarEvent calendarEvent) {
		Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		startDateSortedSet.add(calendarEvent);
	}
	
	public void addEventSeries(User user, EventSeries eventSerie){
		Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		startDateSortedSetOfSeries.add(eventSerie);
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
	
	public CalendarEntry removeEventSeries(EventSeries eventSerie){
		//TODO
		return null;
	}
	
	/**
	 * Only returns an event if the user has privileged access.
	 * @param hash The hash the event produces by calling hashCode()
	 * @return null, if the Event is not found.
	 */
	public CalendarEntry getEventByHash(User user, int hash, Date start) {
		Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		CalendarEvent compareDummy = new CalendarEvent(start, start, "compare-dummy", false);
		Iterator<CalendarEvent> afterStart = startDateSortedSet.tailSet(compareDummy).iterator();
		CalendarEntry e;
		do {
			e = afterStart.next();
		} while (e.hashCode() != hash);
		return e;
	}

	/**
	 * Iterates through the events with a start date after start
	 * 
	 * @param start the date at which to start iterating events
	 * @return an iterator with events starting after start
	 */
	public Iterator<CalendarEvent> iterate(User user, Date start) {
		CalendarEvent compareDummy = new CalendarEvent(start, start, "compare-dummy", false);
		Iterator<CalendarEvent> unfilteredEvents = startDateSortedSet.tailSet(compareDummy).iterator();
		return new ACFilteringIterator(user, unfilteredEvents);
	}
	
	public Iterator<EventSeries> iterateSeries(User user){
		Iterator<EventSeries> allEventSeries = startDateSortedSetOfSeries.iterator();
		return new IteratorWrapper(user, allEventSeries);
	}
	
	/**
	 * Gets a list of events starting within the 24 hour period starting at date;
	 * 
	 * @param date the point in time specifying the start of the 24h period for which events are to be returned
	 * @return a list of the events
	 */
	public List<CalendarEvent> getEventsAt(User user, Date date) {
		Date endDate = new Date(date.getTime()+24*60*60*1000);
		List<CalendarEvent> result = new ArrayList<CalendarEvent>();
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
	public List<SerialEvent> getSerialEventsForDay(User user, Date date){
		List<SerialEvent> result = new ArrayList<SerialEvent>();
		Iterator<EventSeries> iter = iterateSeries(user);
		while (iter.hasNext()) {
			EventSeries es = iter.next();
			if (dateMatches(date, es)) {
				SerialEvent se = new SerialEvent(date, date, es.name, es.isPublic, es);
				result.add(se);
			}
			
		}
		return result;
		
	} 
	
	private boolean dateMatches(Date date, EventSeries es) {
			Repetition repetition = es.getRepetition();
			java.util.Calendar juc = java.util.Calendar.getInstance(new Locale("de", "CH"));
			juc.setTime(es.getStart());
			int weekDayOfEventSerie = juc.DAY_OF_WEEK;
			int yearDayOfEventSerie = juc.DAY_OF_YEAR;
			juc.setTime(date);
			int weekDayOfDate = juc.DAY_OF_WEEK;
			int yearDayOfDate = juc.DAY_OF_YEAR;
			if (repetition.equals(repetition.DAILY)) return true;
			if (repetition.equals(repetition.WEEKLY)) return (weekDayOfEventSerie == weekDayOfDate);
			if (repetition.equals(repetition.YEARLY)) return (yearDayOfEventSerie == yearDayOfDate);
		return false;
	}

	private class ACFilteringIterator implements Iterator<CalendarEvent> {

		private boolean hasNext;
		private CalendarEvent next;
		private Iterator<CalendarEvent> unfilteredEvents;
		private User user;
		
		public ACFilteringIterator(User user, Iterator<CalendarEvent> unfilteredEvents) {
			this.unfilteredEvents = unfilteredEvents;
			this.user = user;
			prepareNext();
		}

		private void prepareNext() {
			hasNext = false;
			if (unfilteredEvents.hasNext()) {
				CalendarEvent ce = unfilteredEvents.next();
				if (!ce.isPublic()) {
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
	private class IteratorWrapper implements Iterator<EventSeries> {

		private boolean hasNext;
		private EventSeries next;
		private Iterator<EventSeries> eventSeries;
		private User user;
		
		public IteratorWrapper(User user, Iterator<EventSeries> eventSeries) {
			this.eventSeries = eventSeries;
			this.user = user;
			prepareNext();
		}

		private void prepareNext() {
			hasNext = false;
			if (eventSeries.hasNext()) {
				EventSeries es = eventSeries.next();
				if (!es.isPublic()) {
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
