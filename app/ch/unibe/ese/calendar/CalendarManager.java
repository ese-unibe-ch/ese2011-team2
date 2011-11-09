package ch.unibe.ese.calendar;

import java.util.Set;
import java.util.SortedSet;

import ch.unibe.ese.calendar.exceptions.CalendarAlreadyExistsException;
import ch.unibe.ese.calendar.exceptions.NoSuchCalendarException;
import ch.unibe.ese.calendar.impl.CalendarManagerImpl;

public abstract class CalendarManager {

	private static CalendarManager instance = new CalendarManagerImpl();

	public abstract void purge(User user);

	public abstract SortedSet<EseCalendar> getCalendarsOf(User user);

	public abstract EseCalendar getCalendar(String name)
			throws NoSuchCalendarException;

	public abstract EseCalendar createCalendar(User user, String name)
			throws CalendarAlreadyExistsException;
	
	public abstract void removeCalendar(String name)
			throws NoSuchCalendarException;

	/**
	 * 
	 * @return the singleton instance
	 */
	public static CalendarManager getInstance() {
		return instance ;
	}

}