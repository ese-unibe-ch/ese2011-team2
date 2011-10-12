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
import ch.unibe.ese.calendar.security.Policy;

public class CalendarManager {
	
	private static CalendarManager instance = new CalendarManager();
	private Map<String, EseCalendar> calendars = new HashMap<String, EseCalendar>();
	
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
	public synchronized EseCalendar createCalendar(User user, String name) throws CalendarAlreayExistsException {
		if (calendars.containsKey(name)) {
			throw new CalendarAlreayExistsException();
		} else {
			EseCalendar calendar = new EseCalendar(name, user);
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
	public synchronized EseCalendar getCalendar(String name) throws NoSuchCalendarException {
		if (calendars.containsKey(name)) {
			return calendars.get(name);
		} else {
			throw new NoSuchCalendarException();
		}
		
	}
	
	public static CalendarManager getInstance() {
		return instance ;
	}

	public synchronized Set<EseCalendar> getCalendarsOf(User user) {
		Set<EseCalendar> result = new HashSet<EseCalendar>();
		for (EseCalendar cal : calendars.values()) {
			if (cal.getOwner().equals(user)) {
				result.add(cal);
			}
		}
		return result;
	}

	public void purge(User user) {
		Policy.getInstance().checkPermission(user, new CalendarAdminPermission());
		calendars.clear();
		
	}

}
