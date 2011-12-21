package controllers;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.PatternSyntaxException;

import play.mvc.Controller;
import play.mvc.With;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.util.UserComparator;

@With(Secure.class)
public class IndexPage extends Controller {
		
	public static void index(String searchRegex) {
		String userName = Security.connected();
		UserManager um = UserManager.getInstance();
		User connectedUser = um.getUserByName(userName);
		Set<User> foundUsers = null;
		if (searchRegex != null) {
			try {
				foundUsers = new TreeSet<User>(new UserComparator(connectedUser));
				foundUsers.addAll(UserManager.getInstance().getUserByRegex(searchRegex));
			} catch (PatternSyntaxException e) {
				//TODO better error handling
				error(e.getMessage());
			}
		}
		CalendarManager calendarManager = CalendarManager.getInstance();
		boolean hasCalendars = !calendarManager.getCalendarsOf(connectedUser).isEmpty();
		render(connectedUser, hasCalendars, foundUsers);
	}
	
}
