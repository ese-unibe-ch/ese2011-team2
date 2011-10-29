package controllers;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.regex.PatternSyntaxException;

import javax.security.auth.Subject;

import play.*;
import play.data.binding.types.CalendarBinder;
import play.mvc.*;
import sun.security.action.GetLongAction;

import ch.unibe.ese.calendar.CalendarEntry;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.EseDateFormat;
import ch.unibe.ese.calendar.EventIteratorMerger;
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.SerialEvent;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.exceptions.CalendarAlreadyExistsException;
import ch.unibe.ese.calendar.exceptions.EventNotFoundException;

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
	
}
