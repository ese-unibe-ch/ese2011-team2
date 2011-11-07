package controllers;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import play.mvc.Controller;
import play.mvc.With;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;

@With(Secure.class)
public class IndexPage extends Controller {
	
	public static Set<String> foundUsers;
	
	public static void index() {
		String userName = Security.connected();
		UserManager um = UserManager.getInstance();
		User user = um.getUserByName(userName);
		CalendarManager calendarManager = CalendarManager.getInstance();
		Set<EseCalendar> userCalendars = calendarManager.getCalendarsOf(user);

		final String token = "You (" + user + ") own: " + userCalendars.size()
				+ " calendars";
		Set<String> foundUsers = IndexPage.foundUsers; //no idea why this is necessary
		render(token, user, userCalendars, foundUsers);
	}
	
	public static void searchUser(String searchRegex) {
		try {
			foundUsers = UserManager.getInstance().getUserByRegex(searchRegex).keySet();
		} catch (PatternSyntaxException e) {
			//TODO error handling
			error(e.getMessage());
		}
		System.out.println(foundUsers);
		index();
	}

	public static void deleteAccount() throws Throwable{
		UserManager um = UserManager.getInstance();
		String userName = Security.connected();
		um.deleteUser(userName);
		Secure.logout();
	}
	
}
