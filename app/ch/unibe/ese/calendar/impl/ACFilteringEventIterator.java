package ch.unibe.ese.calendar.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.Visibility;
import ch.unibe.ese.calendar.security.MyContactAccessPermission;
import ch.unibe.ese.calendar.security.Policy;
import ch.unibe.ese.calendar.security.PrivilegedCalendarAccessPermission;

/**
 * 
 * Filters an Iterator to adapt the element according to the access rights of the user
 *
 */
class ACFilteringEventIterator implements Iterator<CalendarEvent> {

	private boolean hasNext;
	private CalendarEvent next;
	private Iterator<CalendarEvent> unfilteredEvents;
	private User user;
	private String calendarName;
	
	public ACFilteringEventIterator(User user, Iterator<CalendarEvent> unfilteredEvents, String calendarName) {
		this.unfilteredEvents = unfilteredEvents;
		this.user = user;
		this.calendarName = calendarName;
		prepareNext();
	}

	private void prepareNext() {
		hasNext = false;
		if (unfilteredEvents.hasNext()) {
			CalendarEvent ce = unfilteredEvents.next();
			switch (ce.getVisibility()) {
				case PRIVATE:
					if (!Policy.getInstance().hasPermission(user, 
							new PrivilegedCalendarAccessPermission(calendarName))) {
						prepareNext();
						return;
					} else {
						next = ce;
					}
					break;
				case CONTACTSONLY:
					if (!Policy.getInstance().hasPermission(user, 
							new MyContactAccessPermission(calendarName))) {
						next = new CalendarEventImpl(ce.getStart(), ce.getEnd(), 
								"Busy", Visibility.BUSY, ce.getCalendar(), "None");
					} else {
						next = ce;
					}
					break;
				case BUSY:
					if (!Policy.getInstance().hasPermission(user, 
							new PrivilegedCalendarAccessPermission(calendarName))) {
						next = new CalendarEventImpl(ce.getStart(), ce.getEnd(), 
								"Busy", ce.getVisibility(), ce.getCalendar(), "None");
					} else {
						next = ce;
					}
					break;
				default:
					//nothing special
					next = ce;
					break;
				}
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