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
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.EventSeries.Repetition;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.Visibility;
import ch.unibe.ese.calendar.exceptions.EventNotFoundException;
import ch.unibe.ese.calendar.security.MyContactAccessPermission;
import ch.unibe.ese.calendar.security.Policy;
import ch.unibe.ese.calendar.security.PrivilegedCalendarAccessPermission;
import ch.unibe.ese.calendar.util.AbstractCalendar;
import ch.unibe.ese.calendar.util.DateUtils;
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
public class EseCalendarImpl extends AbstractCalendar {
	
	private SortedSet<CalendarEvent> startDateSortedSet = new TreeSet<CalendarEvent>(new StartDateComparator());
	/**
	 * Constructs a calendar. Application typically create and retrieve calendars using the CalendarManager.
	 * 
	 * @param name the name of the calendar
	 * @param owner the owner of the calendar
	 */
	protected EseCalendarImpl(String name, User owner) {
		this.name = name;
		this.owner = owner;
		this.isSelected = true;
	}

	private String name;
	private User owner;
	private boolean isSelected;
	private Map<String, CalendarEvent> eventsById = new HashMap<String, CalendarEvent>();
	private Map<String, EventSeries> seriesById = new HashMap<String, EventSeries>();
	
	@Override
	public String getName() {
		return name;
	}
	
	
	@Override
	public User getOwner() {
		return owner;
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
		seriesById.put(eventSeries.getId(), eventSeries);
		return eventSeries;
	}
	
	
	@Override
	public CalendarEvent removeEvent(User user, String id) {
		Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		CalendarEvent e = getEventById(user, id);
		eventsById.remove(e.getId());
		startDateSortedSet.remove(e);
		return e;
	}
	
	
	
	@Override
	public EventSeries removeEventSeries(User user, String id) {
		Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		CalendarEvent e = getEventById(user, id);
		seriesById.remove(e.getSeries().getId());
		return e.getSeries();
	}


	@Override
	public CalendarEvent getEventById(User user, String id) {
		CalendarEvent ce;
		if (id.indexOf('-') > 0) {
			String seId = id.substring(0, id.indexOf('-'));
			long consecutiveNumber = Long.parseLong(id.substring(id.indexOf('-') + 1));
			ce = seriesById.get(seId).getEventByConsecutiveNumber(consecutiveNumber);
		} else {
			ce = eventsById.get(id);
		}
		if (ce == null)
			throw new EventNotFoundException("No event with this id: " + id);
		if (ce.getVisibility().equals(Visibility.PRIVATE)) {
			Policy.getInstance().checkPermission(user, new PrivilegedCalendarAccessPermission(name));
		}
		return ce;
	}

	@Override
	public Iterator<CalendarEvent> iterate(User user, Date start) {
		Iterator<CalendarEvent> iterateIndividual = iterateIndividualEvents(user, start);
		Iterator<CalendarEvent> iterateSeries = iterateSerialEvents(user, start);
		Iterator<CalendarEvent> unfilteredEvents = new EventIteratorMerger(iterateIndividual, iterateSeries);
		return new ACFilteringEventIterator(user, unfilteredEvents, name);
	}
	
	/**
	 * Iterates through the non-serial events with a start date after start
	 * 
	 * @param start the date at which to start iterating events
	 * @return an iterator with events starting after start
	 */
	private Iterator<CalendarEvent> iterateIndividualEvents(User user, Date start) {
		CalendarEvent compareDummy = new CalendarEventImpl(start, 
				start, "compare-dummy", Visibility.PRIVATE, this, "");
		return startDateSortedSet.tailSet(compareDummy).iterator();

	}
	
	/**
	 * Iterates through the serial events with a start date after start
	 * 
	 * @param start the date at which to start iterating events
	 * @return an iterator with events starting after start
	 */
	private Iterator<CalendarEvent> iterateSerialEvents(User user, Date start) {
		List<Iterator<CalendarEvent>> seriesIterators = new ArrayList<Iterator<CalendarEvent>>(); 
		for (EventSeries series : seriesById.values()) {
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
	
	@Override
	public boolean isSelected(){
		return isSelected;
	}
	
	@Override
	public void select(boolean select){
		this.isSelected = select;
	}
	
}
