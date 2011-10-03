package jobs;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.util.Date;

import javax.security.auth.Subject;

import ch.unibe.ese.calendar.Calendar;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.security.CalendarPolicy;
import play.jobs.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
    
	
	
    public void doJob() {
        UserManager um = UserManager.getIsntance();
        final CalendarManager cm = CalendarManager.getInstance();
        
        Policy.setPolicy(new CalendarPolicy());
        User aaron = um.createUser("aaron", "ese");
       
        Subject.doAs(aaron.getSubject(), new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                Calendar aaroncal = cm.createCalendar("Aarons Kalender");
                java.util.Calendar juc = java.util.Calendar.getInstance();
                juc.set(2011, 11, 23, 20, 15);
                Date start = juc.getTime();
                juc.set(2011, 11, 23, 23, 00);
                Date end = juc.getTime();
                aaroncal.addEvent(new CalendarEvent(start, end, "Toller Film", true));
                return null;
            }
        });
        
        
        
        User judith = um.createUser("judith", "ese");
        User reto = um.createUser("reto", "ese");
        //TODO add a default calendar
        
        
    }
    
}