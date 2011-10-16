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

		createAaronCalendars(um, cm);
		createJudithCalendars(um, cm);
		createErwannCalendars(um, cm);

	}

	private void createAaronCalendars(UserManager um, final CalendarManager cm) {
		User aaron = um.createUser("aaron", "ese");

		EseCalendar aaroncal;
		try {
			aaroncal = cm.createCalendar(aaron, "Aarons Kalender");
		} catch (CalendarAlreadyExistsException e) {
			aaroncal = cm.getCalendar("Aarons Kalender");
		}
		java.util.Calendar juc = java.util.Calendar.getInstance();
		juc.set(2011, 10, 23, 20, 15);
		Date start = juc.getTime();
		juc.set(2011, 10, 23, 23, 00);
		Date end = juc.getTime();
		aaroncal.addEvent(User.ADMIN, new CalendarEvent(start, end,
				"Toller Film", true));
		
		juc.set(2011, 11, 23, 20, 15);
		start = juc.getTime();
		juc.set(2011, 11, 23, 23, 00);
		end = juc.getTime();
		aaroncal.addEvent(User.ADMIN, new CalendarEvent(start, end,
				"Party", true));
		
		juc.set(2011, 12, 23, 20, 15);
		start = juc.getTime();
		juc.set(2011, 12, 24, 04, 00);
		end = juc.getTime();
		aaroncal.addEvent(User.ADMIN, new CalendarEvent(start, end,
				"Moar Party", true));
	}

	private void createJudithCalendars(UserManager um, final CalendarManager cm) {
		User judith = um.createUser("judith", "ese");

		EseCalendar judithcal;
		try {
			judithcal = cm.createCalendar(judith, "Judiths Kalender");
		} catch (CalendarAlreadyExistsException e) {
			judithcal = cm.getCalendar("Judiths Kalender");
		}
		java.util.Calendar juc = java.util.Calendar.getInstance();
		juc.set(2011, 11, 23, 22, 15);
		Date start = juc.getTime();
		juc.set(2011, 11, 23, 23, 00);
		Date end = juc.getTime();
		judithcal.addEvent(User.ADMIN, new CalendarEvent(start, end,
				"Tolle party", true));

	}

	private void createErwannCalendars(UserManager um, final CalendarManager cm) {
		User erwann = um.createUser("erwann", "ese");

		EseCalendar erwanncal;
		try {
			erwanncal = cm.createCalendar(erwann, "Erwanns Kalender");
		} catch (CalendarAlreadyExistsException e) {
			erwanncal = cm.getCalendar("Erwanns Kalender");
		}
		java.util.Calendar juc = java.util.Calendar.getInstance();
		juc.set(2011, 11, 21, 20, 15);
		Date start = juc.getTime();
		juc.set(2011, 11, 21, 23, 00);
		Date end = juc.getTime();
		erwanncal.addEvent(User.ADMIN, new CalendarEvent(start, end,
				"Standard lager", true));

	}
}