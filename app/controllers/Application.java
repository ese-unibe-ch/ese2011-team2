package controllers;

import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.security.auth.Subject;

import play.*;
import play.data.binding.types.CalendarBinder;
import play.mvc.*;
import sun.security.action.GetLongAction;

import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.exceptions.CalendarAlreadyExistsException;


@With(Secure.class)
public class Application extends Controller {
	
    public static void index() {
    	String userName = Security.connected();
    	UserManager um = UserManager.getInstance();
    	User user = um.getUserByName(userName);
    	CalendarManager calendarManager = CalendarManager.getInstance();
    	Set<EseCalendar> userCalendars = calendarManager.getCalendarsOf(user); 
    	
        final String token = "You ("+user+") own: "+userCalendars.size()+" calendars";
		render(token, userCalendars);
    }

    public static void currentCalendar(String name) {
    	java.util.Calendar juc = java.util.Calendar.getInstance(getLocale());
    	juc.setTime(new Date());
    	calendar(name, juc.get(java.util.Calendar.DAY_OF_MONTH), juc.get(java.util.Calendar.MONTH), 
    			juc.get(java.util.Calendar.YEAR));
    }
    
    public static void calendar(String name, int day, int month, int year) {
    	System.out.println("name: "+name);
    	String userName = Security.connected();
    	User user = UserManager.getInstance().getUserByName(userName);
    	CalendarManager calendarManager = CalendarManager.getInstance();
    	final EseCalendar calendar = calendarManager.getCalendar(name);
    	Calendar juc = Calendar.getInstance(getLocale());
    	juc.set(year, month, day, 0,0,0);
    	final Date date = juc.getTime();
  
		Iterator<CalendarEvent> iterator = calendar.getEventsAt(user, date).iterator();

    	CalendarBrowser calendarBrowser = new CalendarBrowser(user, calendar, day, month, year, getLocale());
    	render(iterator, calendar, calendarBrowser);  
    }
    
    /**
     * @return the client locale guessed from accept-language haeder
     */
    private static Locale getLocale() {
    	//TODO make real
    	return new Locale("de", "CH");
    }
    
    public static void users(){   	
    	String userName = Security.connected();
    	User user = UserManager.getInstance().getUserByName(userName);
    	Set<User> users = UserManager.getInstance().getAllUsers();
    	render(user, users);
    }
    
    public static void user(String name){
    	String currentUserName = Security.connected();
    	User currentUser = UserManager.getInstance().getUserByName(currentUserName);
    	User user = UserManager.getInstance().getUserByName(name);
    	Set<EseCalendar> otherCalendars = CalendarManager.getInstance().getCalendarsOf(user);
    	Set<CalendarBrowser> calBrowsers  = new HashSet<CalendarBrowser>();
    	java.util.Calendar juc = java.util.Calendar.getInstance(getLocale());
    	juc.setTime(new Date());
    	int selectedDay = juc.get(java.util.Calendar.DAY_OF_MONTH);
		int year = juc.get(java.util.Calendar.YEAR);
		int month = juc.get(java.util.Calendar.MONTH);
    	for (EseCalendar cal : otherCalendars) {
			calBrowsers.add(new CalendarBrowser(user, cal, selectedDay, month, year, getLocale()));
    	}
    	Set<User> users = UserManager.getInstance().getAllUsers();
    	render(currentUser, user, users, calBrowsers);
    }
    
    public static void createEvent(String calendarName, String name, String startDate, String endDate, boolean isPublic) throws Throwable{

    	System.out.println("creating event");
    	SimpleDateFormat simple = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    	
    	Date sDate = simple.parse(startDate);
    	Date eDate = simple.parse(endDate);
		
		final CalendarEvent event = new CalendarEvent(sDate, eDate, name, isPublic);
		final EseCalendar calendar = CalendarManager.getInstance().getCalendar(calendarName);
		String userName = Security.connected();
    	User user = UserManager.getInstance().getUserByName(userName);

		System.out.println("pre created size "+calendar.getEventsAt(user, new Date(event.getStart().getTime()-2000)).size());
		calendar.addEvent(user, event);
		System.out.println("created event "+event);
		System.out.println("created size "+calendar.getEventsAt(user, new Date(event.getStart().getTime()-2000)).size());

		
		System.out.println("created event  in "+calendarName);
		currentCalendar(calendarName);
    }
    

}