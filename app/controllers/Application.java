package controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.activity.InvalidActivityException;

import play.mvc.Controller;
import play.mvc.With;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.EseDateFormat;
import ch.unibe.ese.calendar.EventIteratorMerger;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.Visibility;
import ch.unibe.ese.calendar.EventSeries.Repetition;
import ch.unibe.ese.calendar.exceptions.EventNotFoundException;

@With(Secure.class)
public class Application extends Controller {
	
	public static int selectedDay, selectedMonth, selectedYear;
	
	public static void currentCalendar(String name) {
		java.util.Calendar juc = java.util.Calendar.getInstance(getLocale());
		juc.setTime(new Date());
		selectedDay = juc.get(java.util.Calendar.DAY_OF_MONTH);
		selectedMonth = juc.get(java.util.Calendar.MONTH);
		selectedYear = juc.get(java.util.Calendar.YEAR);
		calendar(name);
	}

	/**
	 * This method shows the calendar page. 
	 * The selected day is read from instance variables.
	 * 
	 * @param name The name of the calendar
	 */
	public static void calendar(String name) {
		
		System.out.println("name: " + name);
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		CalendarManager calendarManager = CalendarManager.getInstance();
		final EseCalendar calendar = calendarManager.getCalendar(name);
		Calendar juc = Calendar.getInstance(getLocale());
		juc.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
		final Date date = juc.getTime();
		
		Map<User, Boolean> contacts = user.getMyContacts();
		Iterator<User> iterU = contacts.keySet().iterator();
		Set<EseCalendar> selectedUsersCal = new HashSet<EseCalendar>();
		//TODO: Does anybody know how to instantiate this any other way. Googling it right now.
		Iterator iterator = new ArrayList<CalendarEvent>().iterator();
		while (iterU.hasNext()){
			User contact = iterU.next();
			if (contacts.get(contact)){
				Set<EseCalendar> contactCalendars = calendarManager.getCalendarsOf(contact);
				selectedUsersCal.addAll(contactCalendars);
				Iterator<EseCalendar> eseCalendarIter = contactCalendars.iterator();
				while (eseCalendarIter.hasNext()){
					EseCalendar contactCal = eseCalendarIter.next();
					Iterator<CalendarEvent> iteratorCalEvent =  contactCal.getEventsAt(user, date).iterator();
					iterator = new EventIteratorMerger(iterator, iteratorCalEvent); 
				}
			}
		}
		
		CalendarBrowser calendarBrowser = new CalendarBrowser(user, calendar,
				selectedUsersCal, selectedDay, selectedMonth, selectedYear, getLocale());
		
		Set<User> myContacts = user.getSortedContacts();
		render(iterator, calendar, calendarBrowser, myContacts, user);
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
		Set<EseCalendar> otherCalendars = CalendarManager.getInstance()
				.getCalendarsOf(user);
		render(currentUser, user, otherCalendars);
	}
	
	public static void addToContacts(String calendarName, String name) {
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		User userToAdd = UserManager.getInstance().getUserByName(name);
		user.addToMyContacts(userToAdd);
		calendar(calendarName);
	}
	
	public static void removeFromContacts(String calendarName, String name) {
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		User userToRemove = UserManager.getInstance().getUserByName(name);
		try {
			user.removeFromMyContacts(userToRemove);
		} catch (InvalidActivityException e) {
			e.printStackTrace();
		}
		calendar(calendarName);
	}

	public static void createEvent(String calendarName, String name, String startDate, 
			String duration, String visibility, String repetition, String description)
			throws Throwable {
		Date sDate = EseDateFormat.getInstance().parse(startDate);
		int minDur = Integer.parseInt(duration);
		Date eDate = new Date();
		eDate.setTime(sDate.getTime()+1000*60*minDur);
		Visibility vis = Visibility.valueOf(visibility.toUpperCase());
		//Date eDate = EseDateFormat.getInstance().parse(endDate); //old version
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		final EseCalendar calendar = CalendarManager.getInstance().getCalendar(
				calendarName);
		if (repetition.equalsIgnoreCase("never")) {
			calendar.addEvent(user, sDate, eDate, name, vis, description);
		} else {
			calendar.addEventSeries(user, sDate, eDate, name, vis, 
					Repetition.valueOf(repetition.toUpperCase()), description);
		}
		calendar(calendarName);
	}
	
	/**
	 * Shows the calendar with the given date selected.
	 * @param calendarName
	 * @param day
	 * @param month
	 * @param year
	 */
	public static void selectDate(String calendarName, int day, int month, int year) {
		selectedDay = day;
		selectedMonth = month;
		selectedYear = year;
		calendar(calendarName);
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
	 * An event is identified by its unique id. For finding it
	 * easily, we have to know it's startDate.
	 * 
	 * @param calendarName
	 * @param hash hashCode() of the to be deleted event
	 */
	public static void deleteEvent(String calendarName, long id,
			String startDate, boolean isSeries) throws ParseException {

		Date sDate = EseDateFormat.getInstance().parse(startDate);

		final EseCalendar calendar = CalendarManager.getInstance().getCalendar(
				calendarName);
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		CalendarEvent event = calendar.getEventById(user, id, sDate, isSeries);
		if (isSeries) {
			render(user, calendar, event);
		}
		try {
			calendar.removeEvent(user, id, sDate, isSeries);
			calendar(calendarName);
		} catch (EventNotFoundException exception) {
			error(exception.getMessage());
		}
	}
	
	public static void deleteWholeSeries(String calendarName, long id) {
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		final EseCalendar calendar = CalendarManager.getInstance().getCalendar(calendarName);
		calendar.removeEvent(user, id, new Date(0), true);
		calendar(calendarName);
	}
	
	public static void deleteSingleSerialEvent() throws InvalidActivityException {
		throw new InvalidActivityException("Not possible");
	}

	public static void editEvent(String calendarName, long id, 
			String startDate, boolean isSeries, String repetition)
			throws ParseException {
		Date sDate = EseDateFormat.getInstance().parse(startDate);

		final EseCalendar calendar = CalendarManager.getInstance().getCalendar(
				calendarName);
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		Visibility[] visibilities = Visibility.values();
		try {
			CalendarEvent event = calendar.getEventById(user, id, sDate, isSeries);
			//this is quite ugly:
			boolean[] repChecked = new boolean[4];
			if (event.getSeries() == null) {
				repChecked[0]  = true;
			} else {
				repChecked[event.getSeries().getRepetition().ordinal() + 1] = true;
			}
			render(calendar, event, visibilities, repChecked);
		} catch (EventNotFoundException exception) {
			error(exception.getMessage());
		}
	}
	
	public static void saveEditedEvent(String calendarName, long id, String oldStartDate, 
			String name, String startDate, String duration, String visibility, 
			String description, String repetition, boolean wasSeries) 
			throws ParseException {
		
		Date oldDate = EseDateFormat.getInstance().parse(oldStartDate);
		Date sDate = EseDateFormat.getInstance().parse(startDate);
		int minDur = Integer.parseInt(duration);
		Date eDate = new Date();
		eDate.setTime(sDate.getTime()+1000*60*minDur);
		Visibility vis = Visibility.valueOf(visibility.toUpperCase());
		//Date eDate = EseDateFormat.getInstance().parse(endDate);
		final EseCalendar calendar = CalendarManager.getInstance().getCalendar(
				calendarName);
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		calendar.removeEvent(user, id, oldDate, wasSeries);
		if (repetition.equalsIgnoreCase("never")) {
			calendar.addEvent(user, sDate, eDate, name, vis, 
					description);
		} else {
			calendar.addEventSeries(user, sDate, eDate, name, vis, 
					Repetition.valueOf(repetition.toUpperCase()), description);
		}
		selectDate(calendarName, sDate);
	}
	
	/**
	 * First sets all Contacts to unselected then sets all Contacts that have 
	 * their name in checkedContacts[] to selected
	 * 
	 * @param calendarName
	 * @param checkedContacts: all Contacts who's checkbox is selected
	 */
	public static void includeContacts(String calendarName, String[] checkedContacts) {
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		user.unselectAllContacts();
		if (checkedContacts != null) {
			for (String uName: checkedContacts) {
				User u = UserManager.getInstance().getUserByName(uName);
				user.setContactSelection(u, true);
			}
		}	
		calendar(calendarName);
	}

}