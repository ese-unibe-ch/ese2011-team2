package ch.unibe.ese.calendar;

import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import ch.unibe.ese.calendar.exceptions.CalendarAlreayExistsException;
import ch.unibe.ese.calendar.exceptions.NoSuchCalendarException;
import ch.unibe.ese.calendar.security.CalendarAdminPermission;

public class CalendarManager {
	
	private static CalendarManager instance = new CalendarManager();
	private Map<String, Calendar> calendars = new HashMap<String, Calendar>();
	
	private CalendarManager() {
		
	}
	
	/**
	 * Create a calendar with the specified name. This method must be invoked as a Subject with exactly
	 * one CalendarPriciple, i.e. as the Subject returned by <code>User.getSubject</code>.
	 * 
	 * @param name the name of the calendar
	 * @return the newly created calendar
	 * @throws CalendarAlreayExistsException if a calendar with that name already exists
	 */
	public synchronized Calendar createCalendar(String name) throws CalendarAlreayExistsException {
		if (calendars.containsKey(name)) {
			throw new CalendarAlreayExistsException();
		} else {
			Subject currentSubject = Subject.getSubject(AccessController.getContext());
			User owner = UserUtil.getUserForSubject(currentSubject);
			Calendar calendar = new Calendar(name, owner);
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
	public synchronized Calendar getCalendar(String name) throws NoSuchCalendarException {
		if (calendars.containsKey(name)) {
			return calendars.get(name);
		} else {
			throw new NoSuchCalendarException();
		}
		
	}
	
	public static CalendarManager getInstance() {
		return instance ;
	}

	public synchronized Set<Calendar> getCalendarsOf(User user) {
		Set<Calendar> result = new HashSet<Calendar>();
		for (Calendar cal : calendars.values()) {
			if (cal.getOwner().equals(user)) {
				result.add(cal);
			}
		}
		return result;
	}

	public void purge() {
		AccessController.checkPermission(new CalendarAdminPermission());
		calendars.clear();
		
	}

}
