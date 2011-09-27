package ch.unibe.ese.calendar;

import java.security.AccessControlException;
import java.security.AccessController;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.unibe.ese.calendar.security.CalendarWritePermission;

public class Calendar {
	
	private SortedSet<CalendarEvent> startDateSortedSet = new TreeSet<CalendarEvent>(new StartDateComparator());
	
	protected Calendar(String name, User owner) {
		this.name = name;
		this.owner = owner;
	}

	private String name;
	private User owner;
	
	
	
	public String getName() {
		return name;
	}
	
	public User getOwner() {
		return owner;
	}

	public void addEvent(CalendarEvent calendarEvent) {
		AccessController.checkPermission(new CalendarWritePermission(name, ""));
		startDateSortedSet.add(calendarEvent);
		
	}

	public Iterator<CalendarEvent> iterate(Date start) {
		Iterator<CalendarEvent> unfilteredEvents = startDateSortedSet.tailSet(new CalendarEvent(start, null, null, false)).iterator();
		return new ACFilteringIterator(unfilteredEvents);
	}
	
	class ACFilteringIterator implements Iterator<CalendarEvent> {

		private boolean hasNext;
		private CalendarEvent next;
		private Iterator<CalendarEvent> unfilteredEvents;
		
		public ACFilteringIterator(Iterator<CalendarEvent> unfilteredEvents) {
			this.unfilteredEvents = unfilteredEvents;
			prepareNext();
		}

		private void prepareNext() {
			hasNext = false;
			if (unfilteredEvents.hasNext()) {
				CalendarEvent ce = unfilteredEvents.next();
				if (!ce.isPublic()) {
					try {
						AccessController.checkPermission(new CalendarWritePermission(name, ""));
					} catch (AccessControlException e) {
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

}
