package jobs;

import java.security.AccessController;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Date;

import javax.security.auth.Subject;

import play.jobs.Job;
import play.jobs.OnApplicationStart;
import ch.unibe.ese.calendar.Calendar;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.UserUtil;
import ch.unibe.ese.calendar.exceptions.CalendarAlreayExistsException;
import ch.unibe.ese.calendar.security.CalendarPolicy;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
		UserManager um = UserManager.getIsntance();
		final CalendarManager cm = CalendarManager.getInstance();

		System.out.println("context: " + AccessController.getContext());
		System.out.println("domain-combiner: "
				+ AccessController.getContext().getDomainCombiner());

		if (!(Policy.getPolicy() instanceof CalendarPolicy)) {
			Policy.setPolicy(new CalendarPolicy());
		}
		createAaronCalendars(um, cm);
		createJudithCalendars(um, cm);

	}

	private void createAaronCalendars(UserManager um, final CalendarManager cm) {
		User aaron = um.createUser("aaron", "ese");
		Subject.doAs(aaron.getSubject(), new PrivilegedAction<Object>() {
			@Override
			public Object run() {

				Calendar aaroncal;
				try {
					aaroncal = cm.createCalendar("Aarons Kalender");
				} catch (CalendarAlreayExistsException e) {
					aaroncal = cm.getCalendar("Aarons Kalender");
				}
				java.util.Calendar juc = java.util.Calendar.getInstance();
				juc.set(2011, 11, 23, 20, 15);
				Date start = juc.getTime();
				juc.set(2011, 11, 23, 23, 00);
				Date end = juc.getTime();
				aaroncal.addEvent(new CalendarEvent(start, end, "Toller Film",
						true));
				return null;
			}
		});
	}

	private void createJudithCalendars(UserManager um, final CalendarManager cm) {
		User judith = um.createUser("judith", "ese");
		Subject.doAs(judith.getSubject(), new PrivilegedAction<Object>() {
			@Override
			public Object run() {

				Calendar judithcal;
				try {
					judithcal = cm.createCalendar("Judiths Kalender");
				} catch (CalendarAlreayExistsException e) {
					judithcal = cm.getCalendar("Judiths Kalender");
				}
				java.util.Calendar juc = java.util.Calendar.getInstance();
				juc.set(2011, 11, 23, 22, 15);
				Date start = juc.getTime();
				juc.set(2011, 11, 23, 23, 00);
				Date end = juc.getTime();
				judithcal.addEvent(new CalendarEvent(start, end, "Tolle party",
						true));
				return null;
			}
		});

	}

}