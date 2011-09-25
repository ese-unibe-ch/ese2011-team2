package ch.unibe.ese.calendar;

import static org.junit.Assert.assertEquals;

import java.security.AccessControlException;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Date;

import javax.security.auth.Subject;

import org.junit.Before;
import org.junit.Test;

import ch.unibe.ese.calendar.exceptions.CalendarAlreayExistsException;
import ch.unibe.ese.calendar.exceptions.NoSuchCalendarException;
import ch.unibe.ese.calendar.security.CalendarPolicy;

public class CalendarTest {

	private static final String STUDENT_SUSANNE_EXAMS = "student.susanne.exams";
	CalendarManager calendarManager = CalendarManager.getInstance();

	@Before
	public void prepare() {
		Policy.setPolicy(new Policy() {

			@Override
			public PermissionCollection getPermissions(CodeSource codeSource) {
				PermissionCollection result = new Permissions();
				result.add(new AllPermission());
				return result;
			}
		});
		calendarManager.purge();
		Policy.setPolicy(new CalendarPolicy());
		final User user = new User("Susanne");
		Subject.doAs(user.getSubject(), new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				/*Calendar susanneExams = */calendarManager
						.createCalendar(STUDENT_SUSANNE_EXAMS);
				return null;
			}
		});

	}

	@Test(expected = NoSuchCalendarException.class)
	public void canGetUncreateCalendar() {
		calendarManager.getCalendar("student.john.exams");
	}

	@Test(expected = CalendarAlreayExistsException.class)
	public void noDoubleCreation() {
		calendarManager.createCalendar(STUDENT_SUSANNE_EXAMS);
	}

	@Test
	public void calendarHasName() {
		Calendar cal = calendarManager.getCalendar(STUDENT_SUSANNE_EXAMS);
		assertEquals(STUDENT_SUSANNE_EXAMS, cal.getName());
	}

	@Test
	public void calendarHasOwner() {
		final User user = new User("Bob");
		final User owner = Subject.doAs(user.getSubject(),
				new PrivilegedAction<User>() {
					@Override
					public User run() {
						Calendar cal = calendarManager
								.createCalendar("student.bob.exams");
						return cal.getOwner();
					}
				});
		assertEquals(user, owner);
	}

	@Test(expected = AccessControlException.class)
	public void addEventToOtherCalendar() throws Throwable {
		final User user = new User("Bob");
		try {
			Subject.doAs(user.getSubject(),
					new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() {
							Calendar cal = calendarManager
									.getCalendar(STUDENT_SUSANNE_EXAMS);
							java.util.Calendar juc = java.util.Calendar
									.getInstance();
							juc.set(2011, 9, 21, 20, 15);
							Date start = juc.getTime();
							juc.set(2011, 9, 21, 22, 15);
							Date end = juc.getTime();
							CalendarEvent calendarEvent = new CalendarEvent(
									start, end, "Meet Bob (dress up!)");
							cal.addEvent(calendarEvent);
							return null;
						}
					});
		} catch (PrivilegedActionException e) {
			throw e.getCause();
		}

	}

	@Test
	public void addEvent() {
		final User user = new User("Susanne");
		Subject.doAs(user.getSubject(), new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				Calendar cal = calendarManager
						.getCalendar(STUDENT_SUSANNE_EXAMS);
				java.util.Calendar juc = java.util.Calendar.getInstance();
				juc.set(2011, 11, 21, 10, 15);
				Date start = juc.getTime();
				juc.set(2011, 11, 21, 11, 15);
				Date end = juc.getTime();
				CalendarEvent calendarEvent = new CalendarEvent(start, end,
						"Software engineering Exam");
				cal.addEvent(calendarEvent);
				return null;
			}
		});
	}
}
