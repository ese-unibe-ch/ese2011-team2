package ch.unibe.ese.calendar.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.EventSeries.Repetition;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.Visibility;
import ch.unibe.ese.calendar.util.EseDateFormat;

public class VisibilityTest extends UnitTest {
	
	private User owner = new User("Owner");
	private User stalker = new User("Stalker");
	private EseCalendar calendar;
	private CalendarEvent eventPrivate, eventBusy, eventPublic, eventContactsOnly;
	private Date start;

	@Before
	public void setup() throws ParseException {
		CalendarManager.getInstance().purge(owner.ADMIN);
		calendar = CalendarManager.getInstance().createCalendar(owner, "testCal");
		start = EseDateFormat.getInstance().parse("12.11.2011 20:00");
		Date end = EseDateFormat.getInstance().parse("12.11.2011 22:00");
		String eventName = "test1";
		eventPrivate = calendar.addEvent(owner, start, end, eventName, Visibility.PRIVATE, "");
		
		start = EseDateFormat.getInstance().parse("13.11.2011 20:00");
		end = EseDateFormat.getInstance().parse("13.11.2011 22:00");
		eventName = "test2";
		eventBusy = calendar.addEvent(owner, start, end, eventName, Visibility.BUSY, "");
		
		start = EseDateFormat.getInstance().parse("14.11.2011 18:00");
		end = EseDateFormat.getInstance().parse("14.11.2011 22:00");
		eventName = "test3";
		eventPublic = calendar.addEvent(owner, start, end, eventName, Visibility.PUBLIC, "");
		
		start = EseDateFormat.getInstance().parse("15.11.2011 20:00");
		end = EseDateFormat.getInstance().parse("15.11.2011 22:00");
		eventName = "test4";
		eventContactsOnly = calendar.addEvent(owner, start, end, eventName, Visibility.CONTACTSONLY, "");		
	}
	
	@Test
	public void testPrivateVisibility() throws ParseException {
		Date dayStart = EseDateFormat.getInstance().parse("12.11.2011 00:00");
		SortedSet<CalendarEvent> eventsAt = calendar.getEventsAt(owner, dayStart);
		assertEquals(eventPrivate, eventsAt.first());
		eventsAt = calendar.getEventsAt(stalker, dayStart);
		assertTrue(eventsAt.isEmpty());
	}
	
	@Test
	public void testBusyVisibility() throws ParseException {
		Date dayStart = EseDateFormat.getInstance().parse("13.11.2011 00:00");
		SortedSet<CalendarEvent> eventsAt = calendar.getEventsAt(owner, dayStart);
		CalendarEvent eventOwnerSees = eventsAt.first();
		assertEquals(eventBusy, eventOwnerSees);
		assertEquals(eventBusy.getName(), eventOwnerSees.getName());
		
		eventsAt = calendar.getEventsAt(stalker, dayStart);
		CalendarEvent eventStalkerSees = eventsAt.first();
		assertNotSame(eventBusy, eventStalkerSees);
		//Not implemented yet:
		//assertEquals(new JustBusyEvent(eventBusy), eventsAt.first());
		assertEquals("Busy", eventStalkerSees.getName());
		assertEquals("None", eventStalkerSees.getDescription());
	}
	
	@Test
	public void testPublicVisibility() throws ParseException {
		//TODO
	}
	
	@Test
	public void testContactsOnlyVisibility() throws ParseException {
		//TODO
	}
}
	