package controllers;

import java.security.PrivilegedAction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import play.*;
import play.mvc.*;


import ch.unibe.ese.calendar.Calendar;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.exceptions.CalendarAlreayExistsException;

import models.*;

@With(Secure.class)
public class Application extends Controller {
	
    public static void index() {
    	String userName = Security.connected();
    	UserManager um = UserManager.getIsntance();
    	User user = um.getUserByName(userName);
    	CalendarManager calendarManager = CalendarManager.getInstance();
    	Set<Calendar> userCalendars = calendarManager.getCalendarsOf(user);    	

    	CalendarEvent event = new CalendarEvent(new Date(500), 
    			new Date(1500), "an important second", true);
    	
    	
        final String token = "You ("+user+") own: "+userCalendars.size()+" calendars";
		render(token, event, userCalendars);
    }

    public static void calendar(String name) {
    	System.out.println("name: "+name);
    	String userName = Security.connected();
    	User user = UserManager.getIsntance().getUserByName(userName);
    	CalendarManager calendarManager = CalendarManager.getInstance();
    	final Calendar calendar = calendarManager.getCalendar(name);
    	Iterator<CalendarEvent> iterator = Subject.doAs(user.getSubject(), new PrivilegedAction<Iterator<CalendarEvent>>() {
			@Override
			public Iterator<CalendarEvent> run() {		
				return calendar.iterate(new Date(0));
			}
		});
    	render(iterator, calendar);  
    }
    
    public static void users(){   	
    	Set<User> users = UserManager.getIsntance().getAllUsers();
    	//TODO  other users except user
    	render(users);
    }
    
    public static void user(String name){
    	User user = UserManager.getIsntance().getUserByName(name);
    	Set<Calendar> otherCalendars = CalendarManager.getInstance().getCalendarsOf(user);
    	Set<User> users = UserManager.getIsntance().getAllUsers();
    	render(user, users, otherCalendars);
    }
    
    public static void createEvent(String calendarName, String name, String startDate, String endDate, boolean isPublic){

    	SimpleDateFormat simple = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    	Date sDate=null;
    	Date eDate=null;
    	try {
        	sDate = simple.parse(startDate);
        	eDate = simple.parse(endDate);
		} catch (Exception e) { }
		
		CalendarEvent event = new CalendarEvent(sDate, eDate, name, isPublic);
	//	calendar.addEvent(event);
		
		calendar(calendarName);
    }
    

}