package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import ch.unibe.ese.calendar.CalendarEvent;

import models.*;

@With(Secure.class)
public class Application extends Controller {
	
    public static void index() {
    	CalendarEvent event = new CalendarEvent(new Date(500), 
    			new Date(1500), "an important second", true);
        final String token = "a secret token";
		render(token, event);
    }

}