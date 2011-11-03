package ch.unibe.ese.calendar.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.exceptions.CalendarAlreadyExistsException;
import ch.unibe.ese.calendar.exceptions.NoSuchCalendarException;
import ch.unibe.ese.calendar.security.CalendarAdminPermission;
import ch.unibe.ese.calendar.security.Policy;

/**
 * 
 * A CalendarManager manages the calendars in a system
 *
 */
public class CalendarManagerImpl extends CalendarManager {
	
	private Map<String, EseCalendarImpl> calendars = new HashMap<String, EseCalendarImpl>();
	
	public CalendarManagerImpl() {
		
	}
	
	/**
	 * Create a calendar with the specified name. This method must be invoked as a Subject with exactly
	 * one CalendarPriciple, i.e. as the Subject returned by <code>User.getSubject</code>.
	 * 
	 * @param name the name of the calendar
	 * @return the newly created calendar
	 * @throws CalendarAlreadyExistsException if a calendar with that name already exists
	 */
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
	
	/**
	 * Get a calendar with the specified name
	 * 
	 * @param name the name of the calendar
	 * @return the existing calendar
	 * @throws NoSuchCalendarException if no calendar with that name exists
	 */
	@Override
	public synchronized EseCalendar getCalendar(String name) throws NoSuchCalendarException {
		if (calendars.containsKey(name)) {
			return calendars.get(name);
		} else {
			throw new NoSuchCalendarException();
		}
		
	}
	
	/**
	 * Get the calendars own by a user
	 * 
	 * @param user the user for which the calendars are requested
	 * @return the calendars of user
	 */
	@Override
	public synchronized Set<EseCalendar> getCalendarsOf(User user) {
		Set<EseCalendar> result = new HashSet<EseCalendar>();
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

}
