package ch.unibe.ese.calendar;

import java.util.Set;
import java.util.SortedSet;

import ch.unibe.ese.calendar.exceptions.CalendarAlreadyExistsException;
import ch.unibe.ese.calendar.exceptions.NoSuchCalendarException;
import ch.unibe.ese.calendar.impl.CalendarManagerImpl;

public abstract class CalendarManager {

	private static CalendarManager instance = new CalendarManagerImpl();

	/**
	 * permanently deletes all calendars managed my this CalendarManager
	 * @param user the user requesting the operation
	 */
	public abstract void purge(User user);

	/**
	 * Get the calendars own by a user. They are sorted by their name.
	 * 
	 * @param user the user for which the calendars are requested
	 * @return the calendars of user
	 */
	public abstract SortedSet<EseCalendar> getCalendarsOf(User user);
	
	/**
	 * Get a Union of all Calendar of the user. If and how operation modifying
	 * the calendar are supported is not defined.
	 * 
	 * @param user the user for which the calendars are requested
	 * @return the union calendar of user
	 */
	public abstract EseCalendar getUnionCalendarOf(User user);


	/**
	 * Get a calendar with the specified name
	 * 
	 * @param name the name of the calendar
	 * @return the existing calendar
	 * @throws NoSuchCalendarException if no calendar with that name exists
	 */
	public abstract EseCalendar getCalendar(String name)
			throws NoSuchCalendarException;

	/**
	 * Create a calendar with the specified name. This method must be invoked as a Subject with exactly
	 * one CalendarPriciple, i.e. as the Subject returned by <code>User.getSubject</code>.
	 * 
	 * @param name the name of the calendar
	 * @return the newly created calendar
	 * @throws CalendarAlreadyExistsException if a calendar with that name already exists
	 */
	public abstract EseCalendar createCalendar(User user, String name)
			throws CalendarAlreadyExistsException;
	
	/**
	 * Remove a calendar with the specified name.
	 * 
	 * @param name the name of the calendar
	 * @throws NoSuchCalendarException if there is no calendar with that name
	 */
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