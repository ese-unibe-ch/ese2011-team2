package ch.unibe.ese.calendar.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.EventSeries.Repetition;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.Visibility;
import ch.unibe.ese.calendar.exceptions.EventNotFoundException;
import ch.unibe.ese.calendar.security.Policy;
import ch.unibe.ese.calendar.security.PrivilegedCalendarAccessPermission;
import ch.unibe.ese.calendar.util.EventIteratorMerger;
import ch.unibe.ese.calendar.util.StartDateComparator;

/**
 * 
 * A Calendar is a collections of individual events and series of events It provides methods to access 
 * the events at a certain day or to iterate through them. Most methods receive the user for which the 
 * operation is performed the operation is then executed (or denied execution) according the 
 * permissions of the user.
 *
 */
public class EseCalendarImpl extends EseCalendar {
	
	private SortedSet<CalendarEvent> startDateSortedSet = new TreeSet<CalendarEvent>(new StartDateComparator());
	private Set<EventSeries> series = new HashSet<EventSeries>();
	/**
	 * Constructs a calendar. Application typically create and retrieve calendars using the CalendarManager.
	 * 
	 * @param name the name of the calendar
	 * @param owner the owner of the calendar
	 */
	protected EseCalendarImpl(String name, User owner) {
		this.name = name;
		this.owner = owner;
	}

	private String name;
	private User owner;
	private Map<String, CalendarEvent> eventsById = new HashMap<String, CalendarEvent>();
	
	
	@Override
	public String getName() {
		return name;
	}
	
	
	@Override
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
	Set<EventSeries> getStartDateSortedSetOfSeries() {
		return series;
	}

	
	@Override
	public CalendarEvent addEvent(User user, Date start, Date end, String eventName, Visibility visibility, String description) {
		Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		CalendarEvent event = new CalendarEventImpl(start, end, eventName, visibility, this, description);
		startDateSortedSet.add(event);
		eventsById.put(event.getId(), event);
		return event;
	}
	
	
	@Override
	public EventSeries addEventSeries(User user, Date start, Date end, String eventName, Visibility visibility, 
			Repetition repetition, String description){
		Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		EventSeries eventSeries = new EventSeriesImpl(start, end, eventName, visibility, repetition, this, description);
		series.add(eventSeries);
		return eventSeries;
	}
	
	
	@Override
	public CalendarEvent removeEvent(User user, String id) {
		Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		CalendarEvent e = getEventById(user, id);
		//FIXME should remove event not series
		if (e.getSeries() != null) {
			series.remove(e.getSeries());
		} else {
			eventsById.remove(e.getId());
			startDateSortedSet.remove(e);
		}
		return e;
	}
	
	
	
	@Override
	public CalendarEvent getEventById(User user, String id) {
		if (id.indexOf('-') > 0) {
			throw new RuntimeException("getting serial events not yet supported");
		}
		if (!eventsById.containsKey(id)) {
			return null;
		}
		CalendarEvent calendarEvent = eventsById.get(id);
		if (calendarEvent.getVisibility().equals(Visibility.PRIVATE)) {
			Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		}
		return calendarEvent;
	}

	@Override
	public Iterator<CalendarEvent> iterate(User user, Date start) {
		Iterator<CalendarEvent> iterateIndividual = iterateIndividualEvents(user, null);
		Iterator<CalendarEvent> iterateSeries = iterateSerialEvents(user, start);
		return new EventIteratorMerger(iterateIndividual, iterateSeries);
	}
	
	@Override
	public SortedSet<CalendarEvent> getEventsAt(User user, Date dayStart) {
		Date dayEnd = new Date(dayStart.getTime()+24*60*60*1000);
		SortedSet<CalendarEvent> result = new TreeSet<CalendarEvent>(new StartDateComparator());
		Iterator<CalendarEvent> iter = iterate(user, dayStart);
		while (iter.hasNext()) {
			CalendarEvent ce = iter.next();
			if ((ce.getEnd().after(dayStart) && ce.getStart().before(dayEnd)) || 
					(ce.getStart().after(dayStart) && ce.getEnd().before(dayEnd))) {
				result.add(ce);
			}
			if (ce.getStart().after(dayEnd))
				break;
		}
		return result;
	}
	
	/**
	 * Iterates through the non-serial events with a start date after start
	 * 
	 * @param start the date at which to start iterating events
	 * @return an iterator with events starting after start
	 */
	private Iterator<CalendarEvent> iterateIndividualEvents(User user, Date start) {
		//TODO: refactor, if case may not be needed anymore. Better: overload this method.
		if (start == null)
		{
			Iterator<CalendarEvent> unfilteredEvents = startDateSortedSet.iterator();
			return new ACFilteringEventIterator(user, unfilteredEvents);
		} else {
			CalendarEvent compareDummy = new CalendarEventImpl(start, start, "compare-dummy", Visibility.PRIVATE, this, "");
			Iterator<CalendarEvent> unfilteredEvents = startDateSortedSet.tailSet(compareDummy).iterator();
			return new ACFilteringEventIterator(user, unfilteredEvents);
		}
	}
	
	/**
	 * Iterates through the serial events with a start date after start
	 * 
	 * @param start the date at which to start iterating events
	 * @return an iterator with events starting after start
	 */
	private Iterator<CalendarEvent> iterateSerialEvents(User user, Date start) {
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
	private Iterator<EventSeries> iterateSeries(User user){
		Iterator<EventSeries> allEventSeries = series.iterator();
		return new ACFilteringEventSeriesIterator(user, allEventSeries);
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
				if (ce.getVisibility().equals(Visibility.PRIVATE)) {
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
				if (es.getVisibility().equals(Visibility.PRIVATE)) {
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
	@Override
	public EventSeries removeEventSeries(User user, String id) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet implemented");
	}
}