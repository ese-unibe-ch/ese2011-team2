package ch.unibe.ese.calendar.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.exceptions.CalendarAlreadyExistsException;
import ch.unibe.ese.calendar.exceptions.NoSuchCalendarException;
import ch.unibe.ese.calendar.security.CalendarAdminPermission;
import ch.unibe.ese.calendar.security.Policy;
import ch.unibe.ese.calendar.util.EseCalendarComparator;
import ch.unibe.ese.calendar.util.UnionCalendar;

/**
 * 
 * A CalendarManager manages the calendars in a system
 *
 */
public class CalendarManagerImpl extends CalendarManager {
	
	private Map<String, EseCalendarImpl> calendars = new HashMap<String, EseCalendarImpl>();
	
	public CalendarManagerImpl() {
		
	}
	
	
	@Override
	public synchronized EseCalendar createCalendar(User user, String name) throws CalendarAlreadyExistsException {
		if (calendars.containsKey(name)) {
			throw new CalendarAlreadyExistsException();
		} else {
			EseCalendarImpl calendar = new EseCalendarImpl(name, user);
			calendars.put(name, calendar);
			return calendar;
		}	
	}
	
	
	@Override
	public synchronized void removeCalendar(String name) throws NoSuchCalendarException {
		if (calendars.containsKey(name)) {
			calendars.remove(name);
		} else {
			throw new NoSuchCalendarException();
		}
	}
	
	
	@Override
	public synchronized EseCalendar getCalendar(String name) throws NoSuchCalendarException {
		if (calendars.containsKey(name)) {
			return calendars.get(name);
		} else {
			throw new NoSuchCalendarException();
		}
		
	}
	
	
	@Override
	public synchronized SortedSet<EseCalendar> getCalendarsOf(User user) {
		SortedSet<EseCalendar> result = new TreeSet<EseCalendar>(new EseCalendarComparator());
		for (EseCalendarImpl cal : calendars.values()) {
			if (cal.getOwner().equals(user)) {
				result.add(cal);
			}
		}
		return result;
	}

	/**
	 * Purges all calendars managed by this CalendarManager
	 * 
	 * @param user the user requesting the operation
	 */
	@Override
	public void purge(User user) {
		Policy.getInstance().checkPermission(user, new CalendarAdminPermission());
		calendars.clear();
		
	}


	@Override
	public EseCalendar getUnionCalendarOf(User user) {
		Collection<EseCalendar> userCalendars = getCalendarsOf(user);
		Iterator<EseCalendar> iter = userCalendars.iterator();
		EseCalendar mainCalendar = iter.next();
		EseCalendar[] otherCalendars = new EseCalendar[userCalendars.size()-1];
		for (int i = 0; i < userCalendars.size()-1; i++) {
			otherCalendars[i] = iter.next();
		}
		if (otherCalendars.length > 0) {
			return new UnionCalendar(mainCalendar, otherCalendars);
		} else {
			return mainCalendar;
		}
	}


	@Override
	public void unSelectAllCalendars(User user) {
		Iterator<EseCalendar> calendarIterator = getCalendarsOf(user).iterator();
		while (calendarIterator.hasNext()){
			EseCalendar eseCalendar = calendarIterator.next();
			eseCalendar.select(false);
		}
	}

}
