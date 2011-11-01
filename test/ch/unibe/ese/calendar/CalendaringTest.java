package ch.unibe.ese.calendar;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import ch.unibe.ese.calendar.exceptions.CalendarAlreadyExistsException;
import ch.unibe.ese.calendar.exceptions.NoSuchCalendarException;
import ch.unibe.ese.calendar.security.PermissionDeniedException;

/**
 * Requirements from http://ese.unibe.ch/exercises/warming-up
 * 
 * Users have a name: userHasName Calendars have a name: calendarHasName and an
 * owner: calendarHasOwner Calendars have events. An event has a start date/time
 * end date/time a name. An event can be public or private (private events are
 * visible to the owner only). A user can obtain an iterator over the list of
 * events he is allowed to see in a calendar, starting from a specific date. A
 * user can obtain the list of events he is allowed to see in a calendar for a
 * given date.
 * 
 * @author reto
 * 
 */

public class CalendaringTest extends UnitTest {

	private static final String SUSANNE_EVENT_2_DESC = "Meet Oskar";
	private static final String SUSANNE_EVENT_1_DESC = "Software engineering Exam";
	private static final String STUDENT_SUSANNE_EXAMS = "student.susanne.exams";
	CalendarManager calendarManager = CalendarManager.getInstance();

	@Before
	public void prepare() {
		calendarManager.purge(User.ADMIN);
		final User user = new User("Susanne");

		/* Calendar susanneExams = */calendarManager.createCalendar(user,
				STUDENT_SUSANNE_EXAMS);

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

	@Test(expected = CalendarAlreadyExistsException.class)
	public void noDoubleCreation() {
		calendarManager.createCalendar(User.ADMIN, STUDENT_SUSANNE_EXAMS);
	}

	@Test
	public void calendarHasName() {
		EseCalendar cal = calendarManager.getCalendar(STUDENT_SUSANNE_EXAMS);
		assertEquals(STUDENT_SUSANNE_EXAMS, cal.getName());
	}

	@Test
	public void calendarHasOwner() {
		final User user = new User("Bob");
		EseCalendar cal = calendarManager.createCalendar(user,
				"student.bob.exams");
		assertEquals(user, cal.getOwner());
	}

	@Test(expected = PermissionDeniedException.class)
	public void addEventToOtherCalendar() throws Throwable {
		final User user = new User("Bob");
		EseCalendar cal = calendarManager.getCalendar(STUDENT_SUSANNE_EXAMS);
		java.util.Calendar juc = java.util.Calendar.getInstance();
		juc.set(2011, 9, 21, 20, 15);
		Date start = juc.getTime();
		juc.set(2011, 9, 21, 22, 15);
		Date end = juc.getTime();
		cal.addEvent(user, start, end, "Meet Bob (dess up!)", "Public","random Kommentar1");

	}

	@Test
	public void addAndRetrieveEvent() throws Throwable {
		addSusanneEvents();
		// now bob should see exactly one event
		bobIteratesOneEvent();
		// while Susanne sees both events
		susanneGetTwoEventInOder();
		addLaterEventAndGet24hLists();
	}

	private void addLaterEventAndGet24hLists() throws Throwable {
		final User user = new User("Susanne");

		EseCalendar cal = calendarManager.getCalendar(STUDENT_SUSANNE_EXAMS);
		java.util.Calendar juc = java.util.Calendar.getInstance();
		{
			juc.set(2011, 11, 23, 20, 15);
			Date start = juc.getTime();
			juc.set(2011, 11, 23, 21, 15);
			Date end = juc.getTime();
			cal.addEvent(user, start, end, "a later event", "Private","random Kommentar1");
		}
		// two events on day 21
		{
			juc.set(2011, 11, 21, 0, 0);
			Date date = juc.getTime();
			SortedSet<CalendarEvent> events = cal.getEventsAt(user, date);
			assertEquals(2, events.size());
		}
		// one event on day 23
		{
			juc.set(2011, 11, 23, 0, 0);
			Date date = juc.getTime();
			SortedSet<CalendarEvent> events = cal.getEventsAt(user, date);
			assertEquals(1, events.size());
		}

	}

	private void addSusanneEvents() {
		final User user = new User("Susanne");

		EseCalendar cal = calendarManager.getCalendar(STUDENT_SUSANNE_EXAMS);
		java.util.Calendar juc = java.util.Calendar.getInstance();
		{
			juc.set(2011, 11, 21, 10, 15);
			Date start = juc.getTime();
			juc.set(2011, 11, 21, 11, 15);
			Date end = juc.getTime();
			cal.addEvent(user, start, end, SUSANNE_EVENT_1_DESC, "Public","random Kommentar1");
		}
		{
			juc.set(2011, 11, 21, 20, 15);
			Date start = juc.getTime();
			juc.set(2011, 11, 21, 21, 15);
			Date end = juc.getTime();
			cal.addEvent(user, start, end, SUSANNE_EVENT_2_DESC, "Private","random Kommentar1");
		}

	}

	private void bobIteratesOneEvent() throws Throwable {
		final User user = new User("Bob");

		EseCalendar cal = calendarManager.getCalendar(STUDENT_SUSANNE_EXAMS);
		java.util.Calendar juc = java.util.Calendar.getInstance();
		juc.set(2011, 11, 21, 0, 0);
		Date start = juc.getTime();
		Iterator<CalendarEvent> iter = cal.iterateIndividualEvents(user, start);
		assertTrue(iter.hasNext());
		assertNotNull(iter.next());
		assertFalse(iter.hasNext());

	}

	private void susanneGetTwoEventInOder() throws Throwable {
		final User user = new User("Susanne");

		EseCalendar cal = calendarManager.getCalendar(STUDENT_SUSANNE_EXAMS);
		java.util.Calendar juc = java.util.Calendar.getInstance();
		juc.set(2011, 11, 21, 0, 0);
		Date start = juc.getTime();
		Iterator<CalendarEvent> iter = cal.iterateIndividualEvents(user, start);
		assertTrue(iter.hasNext());
		CalendarEvent first = iter.next();
		assertEquals(SUSANNE_EVENT_1_DESC, first.getName());
		assertTrue(iter.hasNext());
		CalendarEvent second = iter.next();
		assertEquals(SUSANNE_EVENT_2_DESC, second.getName());
		assertFalse("no third element", iter.hasNext());

	}

}
