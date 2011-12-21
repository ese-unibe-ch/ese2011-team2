package controllers;

import java.util.SortedSet;

import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import play.i18n.Lang;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class UserPage extends Controller{

	public static void user() {
		String currentUserName = Security.connected();
		User connectedUser = UserManager.getInstance().getUserByName(
				currentUserName);
		SortedSet<EseCalendar> calendars = CalendarManager.getInstance()
				.getCalendarsOf(connectedUser);
		String lang = Lang.get();
		render(connectedUser, calendars, lang);
	}
	
	public static void changeLanguage(String language){
		Lang.change(language);
		user();
	}
	
	public static void deleteCalendar(String calendarName) {
		CalendarManager calendarManager = CalendarManager.getInstance();
		try {
			calendarManager.removeCalendar(calendarName);
		} catch (Exception e) {
			error(e.getMessage());
		}
		user();
		}
	
	public static void addCalendar(String calendarName) {
		String userName = Security.connected();
		UserManager um = UserManager.getInstance();
		User connectedUser = um.getUserByName(userName);
		CalendarManager calendarManager = CalendarManager.getInstance();
		try {
			calendarManager.createCalendar(connectedUser, calendarName);
		} catch (Exception e) {
			error(e.getMessage());
		}
		user();
	}
	
	public static void deleteAccount() throws Throwable {
		UserManager um = UserManager.getInstance();
		String userName = Security.connected();
		um.deleteUser(userName);
		Secure.logout();
	}
	
	public static void prepareDeleteAccount() {
		render();
	}
}
