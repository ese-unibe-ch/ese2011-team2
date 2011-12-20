package ch.unibe.ese.calendar.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.Visibility;
import ch.unibe.ese.calendar.util.EseDateFormat;
import ch.unibe.ese.calendar.util.UnionCalendar;

import play.test.UnitTest;

public class UnionCalendarTest extends UnitTest {
	
	User user;
	UnionCalendar unionCal;
	EseCalendar mainCal;
	EseCalendar otherCal;
	CalendarEvent eventInMainCal;
	CalendarEvent eventInOtherCal;
	Date compareDate;
	
	@Before
	public void setUp() throws ParseException {
		new User("dummy");
		mainCal = new EseCalendarImpl("Home", user);
		otherCal = new EseCalendarImpl("Uni", user);
		eventInMainCal = mainCal.addEvent(user.ADMIN, new Date(), new Date(), "mainDummy", Visibility.PUBLIC, "this is a test event");
		eventInOtherCal = otherCal.addEvent(user.ADMIN, new Date(), new Date(), "otherDummy", Visibility.PUBLIC, "this is another test event");
		unionCal = new UnionCalendar(mainCal, otherCal);
		compareDate = EseDateFormat.getInstance().parse("12.01.2000 12:00");
	}
	
	@Test
	public void getName() {
		assertEquals("Home", unionCal.getName());
	}
	
	@Test
	public void getOwner() {
		assertEquals(mainCal.getOwner(), unionCal.getOwner());
		assertEquals(otherCal.getOwner(), unionCal.getOwner());
	}
	
	@Test
	public void iterate() {
		Iterator<CalendarEvent> unionIterator = unionCal.iterate(user, compareDate);
		assertEquals(eventInMainCal, unionIterator.next());
		assertEquals(eventInOtherCal, unionIterator.next());
	}
	
	@Test
	public void removeEvent() {
		unionCal.removeEvent(user.ADMIN, eventInMainCal.getId());
		unionCal = new UnionCalendar(mainCal, otherCal);
		Iterator<CalendarEvent> unionIterator = unionCal.iterate(user, compareDate);
		assertEquals(eventInOtherCal, unionIterator.next());
		assertFalse(mainCal.iterate(user, compareDate).hasNext());
	}
	
}

