package ch.unibe.ese.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.AccessControlException;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.Subject;

import org.junit.Before;
import org.junit.Test;

import ch.unibe.ese.calendar.exceptions.CalendarAlreayExistsException;
import ch.unibe.ese.calendar.exceptions.NoSuchCalendarException;
import ch.unibe.ese.calendar.security.CalendarPolicy;

/**
 * Requirements from http://ese.unibe.ch/exercises/warming-up
 * 
 * Users have a name: userHasName
 * Calendars have a name: calendarHasName
 * and an owner: calendarHasOwner
    Calendars have events.
    An event has a
        start date/time
        end date/time
        a name. 
    An event can be public or private (private events are visible to the owner only).
  * A user can obtain an iterator over the list of events he is allowed to see in a calendar, starting from a specific date.
  * A user can obtain the list of events he is allowed to see in a calendar for a given date. 
 * @author reto
 *
 */

public class CalendaringTest {

	private static final String SUSANNE_EVENT_2_DESC = "Meet Oskar";
	private static final String SUSANNE_EVENT_1_DESC = "Software engineering Exam";
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
	
	@Test
	public void userHasName() {
		String name = "Joe Testing";
		User user = new User(name);
		assertEquals(name, user.getName());
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
									start, end, "Meet Bob (dress up!)", true);
							cal.addEvent(calendarEvent);
							return null;
						}
					});
		} catch (PrivilegedActionException e) {
			throw e.getCause();
		}

	}

	@Test
	public void addAndRetrieveEvent() throws Throwable {
		addSusanneEvents();
		//now bob should see exactly one event
		bobIteratesOneEvent();
		//while Susanne sees both events
		susanneGetTwoEventInOder();
		addLaterEventAndGet24hLists();
	}

	private void addLaterEventAndGet24hLists() throws Throwable {
		final User user = new User("Susanne");
		try {
			Subject.doAs(user.getSubject(), new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() {
					Calendar cal = calendarManager
							.getCalendar(STUDENT_SUSANNE_EXAMS);
					java.util.Calendar juc = java.util.Calendar.getInstance();
					{
						juc.set(2011, 11, 23, 20, 15);
						Date start = juc.getTime();
						juc.set(2011, 11, 23, 21, 15);
						Date end = juc.getTime();
						CalendarEvent calendarEvent = new CalendarEvent(start, end,
								"a later event", false);
						cal.addEvent(calendarEvent);
					}
					//two events on day 21
					{
						juc.set(2011, 11, 21, 0, 0);
						Date date = juc.getTime();
						List<CalendarEvent> events = cal.getEventsAt(date);
						assertEquals(2, events.size());
					}
					//one event on day 23
					{
						juc.set(2011, 11, 23, 0, 0);
						Date date = juc.getTime();
						List<CalendarEvent> events = cal.getEventsAt(date);
						assertEquals(1, events.size());
					}
					return null;
				}
			});
		} catch (PrivilegedActionException e) {
			throw e.getCause();
		}
	}

	private void addSusanneEvents() {
		final User user = new User("Susanne");
		Subject.doAs(user.getSubject(), new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				Calendar cal = calendarManager
						.getCalendar(STUDENT_SUSANNE_EXAMS);
				java.util.Calendar juc = java.util.Calendar.getInstance();
				{
					juc.set(2011, 11, 21, 10, 15);
					Date start = juc.getTime();
					juc.set(2011, 11, 21, 11, 15);
					Date end = juc.getTime();
					CalendarEvent calendarEvent = new CalendarEvent(start, end,
							SUSANNE_EVENT_1_DESC, true);
					cal.addEvent(calendarEvent);
				}
				{
					juc.set(2011, 11, 21, 20, 15);
					Date start = juc.getTime();
					juc.set(2011, 11, 21, 21, 15);
					Date end = juc.getTime();
					CalendarEvent calendarEvent = new CalendarEvent(start, end,
							SUSANNE_EVENT_2_DESC, false);
					cal.addEvent(calendarEvent);
				}
				return null;
			}
		});
	}
	
	private void bobIteratesOneEvent() throws Throwable {
		final User user = new User("Bob");
		try {
			Subject.doAs(user.getSubject(), new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() {
					Calendar cal = calendarManager
							.getCalendar(STUDENT_SUSANNE_EXAMS);
					java.util.Calendar juc = java.util.Calendar.getInstance();
					juc.set(2011, 11, 21, 0, 0);
					Date start = juc.getTime();
					Iterator<CalendarEvent> iter = cal.iterate(start);
					assertTrue(iter.hasNext());
					assertNotNull(iter.next());
					assertFalse(iter.hasNext());
					return null;
				}
			});
		} catch (PrivilegedActionException e) {
			throw e.getCause();
		}	
	}
	
	private void susanneGetTwoEventInOder() throws Throwable {
		final User user = new User("Susanne");
		try {
			Subject.doAs(user.getSubject(), new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() {
					Calendar cal = calendarManager
							.getCalendar(STUDENT_SUSANNE_EXAMS);
					java.util.Calendar juc = java.util.Calendar.getInstance();
					juc.set(2011, 11, 21, 0, 0);
					Date start = juc.getTime();
					Iterator<CalendarEvent> iter = cal.iterate(start);
					assertTrue(iter.hasNext());
					CalendarEvent first = iter.next();
					assertEquals(SUSANNE_EVENT_1_DESC, first.getName());
					assertTrue(iter.hasNext());
					CalendarEvent second = iter.next();
					assertEquals(SUSANNE_EVENT_2_DESC, second.getName());
					assertFalse("no third element", iter.hasNext());
					return null;
				}
			});
		} catch (PrivilegedActionException e) {
			throw e.getCause();
		}
	}
	
}
