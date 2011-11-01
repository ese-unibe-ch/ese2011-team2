package jobs;

import java.util.Date;

import play.jobs.Job;
import play.jobs.OnApplicationStart;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.exceptions.CalendarAlreadyExistsException;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
		UserManager um = UserManager.getInstance();
		final CalendarManager cm = CalendarManager.getInstance();

		createSomeCalendars(um, cm);

	}

	private void createSomeCalendars(UserManager um, final CalendarManager cm) {

		User aaron = um.createUser("aaron", "ese");
		
		EseCalendar aaroncal;
		try {
			aaroncal = cm.createCalendar(aaron, "Aarons Calendar");
		} catch (CalendarAlreadyExistsException e) {
			aaroncal = cm.getCalendar("Aarons Calendar");
		}
		java.util.Calendar juc = java.util.Calendar.getInstance();
		juc.set(2011, 10, 23, 20, 15);
		Date start = juc.getTime();
		juc.set(2011, 10, 23, 23, 00);
		Date end = juc.getTime();
		aaroncal.addEvent(User.ADMIN, start, end, "Toller Film", "Public", "der Film ist wirklich super");
		
		juc.set(2011, 11, 23, 20, 15);
		start = juc.getTime();
		juc.set(2011, 11, 23, 23, 00);
		end = juc.getTime();
		aaroncal.addEvent(User.ADMIN, start, end, "Tolle Party", "Public", "Die Fetzen werden fliegen");
		
		juc.set(2011, 12, 23, 20, 15);
		start = juc.getTime();
		juc.set(2011, 12, 24, 04, 00);
		end = juc.getTime();
		aaroncal.addEvent(User.ADMIN, start, end, "MOAR PARTY!", "Public","random Kommentar1");
	
		User judith = um.createUser("judith", "ese");

		EseCalendar judithcal;
		try {
			judithcal = cm.createCalendar(judith, "Judiths Calendar");
		} catch (CalendarAlreadyExistsException e) {
			judithcal = cm.getCalendar("Judiths Calendar");
		}
		juc.set(2011, 11, 23, 22, 15);
		start = juc.getTime();
		juc.set(2011, 11, 23, 23, 00);
		end = juc.getTime();
		judithcal.addEvent(User.ADMIN, start, end, "Movienight", "Public", "random Kommentar2");

	
		User erwann = um.createUser("erwann", "ese");

		EseCalendar erwanncal;
		try {
			erwanncal = cm.createCalendar(erwann, "Erwanns Calendar");
		} catch (CalendarAlreadyExistsException e) {
			erwanncal = cm.getCalendar("Erwanns Calendar");
		}
		juc.set(2011, 11, 21, 20, 15);
		start = juc.getTime();
		juc.set(2011, 11, 21, 23, 00);
		end = juc.getTime();
		erwanncal.addEvent(User.ADMIN, start, end, "Standardlager", "Public", "random Kommentar3");
		
		erwann.addToMyContacts(judith);
		erwann.addToMyContacts(aaron);

	}
}