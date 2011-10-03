package controllers;

import java.util.Date;
import java.util.Set;

import play.*;
import play.mvc.*;


import ch.unibe.ese.calendar.Calendar;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;

import models.*;

@With(Secure.class)
public class Application extends Controller {
	
    public static void index() {
    	String userName = Security.connected();
    	User user = UserManager.getIsntance().getUserByName(userName);
    	CalendarManager calendarManager = CalendarManager.getInstance();
    	Set<Calendar> userCalendars = calendarManager.getCalendarsOf(user);
    	   	
    	CalendarEvent event = new CalendarEvent(new Date(500), 
    			new Date(1500), "an important second", true);
    	
    	
        final String token = "You ("+user+") own: "+userCalendars.size()+" calendars";
        //TODO return user calendars
		render(token, event, userCalendars);
    }

}