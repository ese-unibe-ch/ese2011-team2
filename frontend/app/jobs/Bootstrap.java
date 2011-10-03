package jobs;
import java.util.Date;

import javax.security.auth.Subject;

import ch.unibe.ese.calendar.Calendar;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import play.jobs.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
    
	
	
    public void doJob() {
        UserManager um = UserManager.getIsntance();
        CalendarManager cm = CalendarManager.getInstance();
        
        User aaron = um.createUser("aaron", "ese");
        Calendar aaroncal = cm.createCalendar("Aarons Kalender");
        java.util.Calendar juc = java.util.Calendar.getInstance();
        juc.set(2011, 11, 23, 20, 15);
        Date start = juc.getTime();
        juc.set(2011, 11, 23, 23, 00);
        Date end = juc.getTime();
        aaroncal.addEvent(new CalendarEvent(start, end, "Toller Film", true));
        
        User judith = um.createUser("judith", "ese");
        User reto = um.createUser("reto", "ese");
        //TODO add a default calendar
        
        
    }
    
}