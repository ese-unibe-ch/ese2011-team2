package ch.unibe.ese.calendar;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

import ch.unibe.ese.calendar.exceptions.CalendarAlreadyExistsException;
import ch.unibe.ese.calendar.exceptions.NoSuchCalendarException;
import ch.unibe.ese.calendar.security.PermissionDeniedException;

public class CalendarManagerTest extends UnitTest {
	
	CalendarManager calManager;
	User user;
	
	@Before
	public void setup() {
		calManager = CalendarManager.getInstance();
		user = new User("dummy");
	}
	
	@After
	public void teardown() {
		calManager.purge(user.ADMIN);
	}
	
	@Test
	public void createCalendar() {
		EseCalendar calendar = calManager.createCalendar(user, "Uni");
		assertTrue(calManager.getCalendar("Uni") != null);
		assertEquals(calendar, calManager.getCalendar("Uni"));
		assertEquals("Uni", calManager.getCalendar("Uni").getName());
	}
	
	@Test
	public void getCalendarsOfAGivenUser() {
		EseCalendar homeCalendar = calManager.createCalendar(user, "Home");
		assertTrue(calManager.getCalendarsOf(user).contains(homeCalendar));
		EseCalendar workCalendar = calManager.createCalendar(user, "Work");
		assertTrue(calManager.getCalendarsOf(user).contains(homeCalendar)
				&& calManager.getCalendarsOf(user).contains(workCalendar));
	}
	
	@Test
	public void purgeAGivenUsersCalendars() {
		EseCalendar cal = calManager.createCalendar(user, "SoonToBePurgedCal");
		assertFalse(calManager.getCalendarsOf(user).isEmpty());
		assertEquals(cal, calManager.getCalendar("SoonToBePurgedCal"));
		calManager.purge(user.ADMIN);
		assertTrue(calManager.getCalendarsOf(user).isEmpty());
	}
	
	@Test (expected=PermissionDeniedException.class)
	public void tryToPurgeAGivenUsersCalendarsWithoutPermission() {
		EseCalendar cal = calManager.createCalendar(user, "NotToBePurgedCal");
		assertEquals(cal, calManager.getCalendar("NotToBePurgedCal"));
		assertFalse(calManager.getCalendarsOf(user).isEmpty());
		calManager.purge(user);
		assertTrue(calManager.getCalendarsOf(user).isEmpty());
	}
	
	@Test (expected=NoSuchCalendarException.class)
	public void tryToGetNonExistentCalendar() {
		calManager.getCalendar("NonExistentCal");
	}
	
	@Test (expected=CalendarAlreadyExistsException.class)
	public void tryToCreateAlreadyExistentCalendar() {
		@SuppressWarnings("unused")
		EseCalendar cal1 = calManager.createCalendar(user, "Home");
		@SuppressWarnings("unused")
		EseCalendar cal2 = calManager.createCalendar(user, "Home");
	}

}
