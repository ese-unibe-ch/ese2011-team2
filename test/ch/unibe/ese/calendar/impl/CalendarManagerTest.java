package ch.unibe.ese.calendar.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.exceptions.CalendarAlreadyExistsException;
import ch.unibe.ese.calendar.exceptions.NoSuchCalendarException;
import ch.unibe.ese.calendar.exceptions.CanNotRemoveLastCalendarException;
import ch.unibe.ese.calendar.impl.CalendarManagerImpl;
import ch.unibe.ese.calendar.security.PermissionDeniedException;

public class CalendarManagerTest extends UnitTest {
	
	CalendarManager calManager;
	User user;
	
	@Before
	public void setup() {
		calManager = CalendarManagerImpl.getInstance();
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
	
	@Test
	public void retrieveACorrectlySortedSetOfCalendars() {
		EseCalendar calC = calManager.createCalendar(user, "c");
		@SuppressWarnings("unused")
		EseCalendar calB = calManager.createCalendar(user, "b");
		EseCalendar calA = calManager.createCalendar(user, "a");
		assertEquals(calA, calManager.getCalendarsOf(user).first());
		assertEquals(calC, calManager.getCalendarsOf(user).last());
	}
	
	@Test(expected=CanNotRemoveLastCalendarException.class)
	public void removingOnlyCalendarThrowsException() {
		EseCalendar cal = calManager.createCalendar(user, "calToRemove");
		assertTrue(calManager.getCalendarsOf(user).contains(cal));
		calManager.removeCalendar("calToRemove");
	}
	
	@Test (expected=NoSuchCalendarException.class)
	public void tryToRemoveNonExistentCalendar() {
		calManager.removeCalendar("NonExistentCalendar");
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
	
	@Test (expected=NoSuchCalendarException.class)
	public void testRemoveInexistantCalendar(){
		EseCalendar myCalendar = calManager.createCalendar(user, "myCalendar");
		assertTrue(calManager.getCalendarsOf(user).contains(myCalendar));
		calManager.removeCalendar("NonExistentCal");
	}
	
	@Test 
	public void testUnselectAllCalendars(){
		EseCalendar myCalendar = calManager.createCalendar(user, "myCalendar");
		assertTrue(myCalendar.isSelected());
		calManager.unSelectAllCalendars(user);
		assertFalse(myCalendar.isSelected());
		myCalendar.select(true);
		assertTrue(myCalendar.isSelected());
		
	}

}
