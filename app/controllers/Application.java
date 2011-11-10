package controllers;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.activity.InvalidActivityException;

import play.mvc.Controller;
import play.mvc.With;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.EventSeries.Repetition;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.Visibility;
import ch.unibe.ese.calendar.exceptions.EventNotFoundException;
import ch.unibe.ese.calendar.util.EseDateFormat;
import ch.unibe.ese.calendar.util.EventIteratorMerger;

@With(Secure.class)
public class Application extends Controller {
	
	public static int selectedDay, selectedMonth, selectedYear;
	
	/**
	 * Will display the current calendar of a specified user.
	 * @param userName
	 */
	public static void currentCalendar(String userName) {
		java.util.Calendar juc = java.util.Calendar.getInstance(getLocale());
		juc.setTime(new Date());
		selectedDay = juc.get(java.util.Calendar.DAY_OF_MONTH);
		selectedMonth = juc.get(java.util.Calendar.MONTH);
		selectedYear = juc.get(java.util.Calendar.YEAR);
		calendar(userName);
	}

	/**
	 * This method shows the calendar page of a specified user. 
	 * The selected day is read from instance variables.
	 * 
	 * @param name The name of the calendar
	 */
	public static void calendar(String userName) {
		System.out.println("name: " + userName);
		String connectedUserName = Security.connected();
		User connectedUser = UserManager.getInstance().getUserByName(connectedUserName);
		User user = UserManager.getInstance().getUserByName(userName);
		CalendarManager calendarManager = CalendarManager.getInstance();
		//done this way to display a calendar directly. triedo will probably change this later
		EseCalendar calendar = selectCalendarToDisplay(user, connectedUser);
		Calendar juc = Calendar.getInstance(getLocale());
		juc.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
		final Date date = juc.getTime();
		Map<User, Boolean> myContactsMap = connectedUser.getMyContacts();
		Iterator<User> iterMyContacts = myContactsMap.keySet().iterator();
		Set<EseCalendar> selectedUsersCal = new HashSet<EseCalendar>();
		Iterator iterator = Collections.EMPTY_LIST.iterator();
		if (!user.equals(connectedUser)) {
			iterator = calendar.getEventsAt(user, date).iterator();
		} else {
			while (iterMyContacts.hasNext()){
				User contact = iterMyContacts.next();
				if (myContactsMap.get(contact)){
					Set<EseCalendar> contactCalendars = new HashSet <EseCalendar>();
					if (contact.equals(user)){
						contactCalendars.add(calendar);
					}
					else {
						 contactCalendars = calendarManager.getCalendarsOf(contact);
					}
					selectedUsersCal.addAll(contactCalendars);
					Iterator<EseCalendar> eseCalendarIter = contactCalendars.iterator();
					while (eseCalendarIter.hasNext()){
						EseCalendar contactCal = eseCalendarIter.next();
						Iterator<CalendarEvent> iteratorCalEvent =  contactCal.getEventsAt(user, date).iterator();
						iterator = new EventIteratorMerger(iterator, iteratorCalEvent); 
					}
				}
			}
		}
		CalendarBrowser calendarBrowser = new CalendarBrowser(user, calendar,
				selectedUsersCal, selectedDay, selectedMonth, selectedYear, getLocale());
		
		Set<User> myContacts = connectedUser.getSortedContacts();
		render(iterator, calendar, calendarBrowser, myContacts, connectedUser);
	}
	
	/**
	 * Selects the calendar to display according to the user.
	 * @param user
	 * @param connectedUser
	 * @return
	 */
	private static EseCalendar selectCalendarToDisplay(User user,
			User connectedUser) {
		CalendarManager calendarManager = CalendarManager.getInstance();
		SortedSet<EseCalendar> connectedUserCalendars = calendarManager.getCalendarsOf(connectedUser);
		EseCalendar calendar = connectedUserCalendars.iterator().next();
		if (!user.equals(connectedUser)) {
			SortedSet calendars = calendarManager.getCalendarsOf(user);
			if (calendars.size()<2) {
				calendar = (EseCalendar) calendars.iterator().next();
			} else {
				calendar = calendarManager.getUnionCalendarOf(user);
			}
		}
		return calendar;
	}

	/**
	 * @return the client locale guessed from accept-language haeder
	 */
	private static Locale getLocale() {
		// TODO make real
		return new Locale("en", "CH");
	}

	public static void user(String name) {
		String currentUserName = Security.connected();
		User currentUser = UserManager.getInstance().getUserByName(
				currentUserName);
		User user = UserManager.getInstance().getUserByName(name);
		SortedSet<EseCalendar> otherCalendars = CalendarManager.getInstance()
				.getCalendarsOf(user);
		Iterator<User> myContactsIterator = currentUser.getSortedContacts().iterator();
		render(currentUser, user, otherCalendars, myContactsIterator);
	}
	
	public static void addToContacts(String name) {
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		User userToAdd = UserManager.getInstance().getUserByName(name);
		user.addToMyContacts(userToAdd);
		calendar(name);
	}
	
	/**
	 * Remove the user with the specified name from myContacts.
	 * @param name of the user to remove
	 */
	public static void removeFromContacts(String name) {
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		User userToRemove = UserManager.getInstance().getUserByName(name);
		try {
			user.removeFromMyContacts(userToRemove);
		} catch (InvalidActivityException e) {
			e.printStackTrace();
		}
		calendar(name);
	}
	
	/**
	 * Shows the calendar with the given date selected.
	 * @param calendarName
	 * @param day
	 * @param month
	 * @param year
	 */
	public static void selectDate(String userName, int day, int month, int year) {
		selectedDay = day;
		selectedMonth = month;
		selectedYear = year;
		calendar(userName);
	}
	
	/**
	 * -- this method is not in use right now --
	 * 
	 * Does same as selectDate(String calendarName, int day, int month, int year),
	 * but with other parameters. Small helper method.
	 * Shows the calendar with the given Date selected.	 * 
	 * @param calendarName
	 * @param date 
	 */
	private static void selectDate(String calendarName, Date date) {
		Calendar juc = Calendar.getInstance(getLocale());
		juc.setTime(date);
		selectDate(calendarName, juc.get(java.util.Calendar.DAY_OF_MONTH),
				juc.get(java.util.Calendar.MONTH),
				juc.get(java.util.Calendar.YEAR));
	}
	
	/**
	 * First sets all Contacts to unselected then sets all Contacts that have 
	 * their name in checkedContacts[] to selected
	 * 
	 * @param checkedContacts: all Contacts who's checkbox is selected
	 */
	public static void includeContacts(String[] checkedContacts) {
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		user.unselectAllContacts();
		if (checkedContacts != null) {
			for (String uName: checkedContacts) {
				User u = UserManager.getInstance().getUserByName(uName);
				user.setContactSelection(u, true);
			}
		}	
		calendar(userName);
	}
	
	/**
	*
	*
	*/
	public static void deleteCalendar(String calendarName) {
		CalendarManager calendarManager = CalendarManager.getInstance();
		calendarManager.removeCalendar(calendarName);
		String userName = Security.connected();
		user(userName);
		
	}
	
	/**
	*
	*
	*/
	public static void addCalendar(String calendarName) {
		String userName = Security.connected();
		UserManager um = UserManager.getInstance();
		User user = um.getUserByName(userName);
		CalendarManager calendarManager = CalendarManager.getInstance();
		
		calendarManager.createCalendar(user, calendarName);
		user(userName);
	}

}