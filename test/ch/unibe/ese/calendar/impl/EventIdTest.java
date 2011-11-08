package ch.unibe.ese.calendar.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.EventSeries.Repetition;
import ch.unibe.ese.calendar.exceptions.EventNotFoundException;
import ch.unibe.ese.calendar.util.EseDateFormat;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.Visibility;

public class EventIdTest extends UnitTest {
	
	private EseCalendarImpl calendar;
	private User user;
	private CalendarEvent event1, event2, event3;
	private EventSeries es;
	private Date start;
	
	
	@Before
	public void setup() throws ParseException {
		user = new User("dummy");
		calendar = new EseCalendarImpl("TestCalendar", user);
		start = EseDateFormat.getInstance().parse("12.11.2011 20:00");
		Date end = EseDateFormat.getInstance().parse("12.11.2011 22:00");
		String eventName = "test1";
		event1 = calendar.addEvent(user.ADMIN, start, end, eventName, Visibility.PRIVATE, "");
		start = EseDateFormat.getInstance().parse("12.1.2010 20:00");
		end = EseDateFormat.getInstance().parse("12.1.2010 22:00");
		eventName = "test2";
		event2 = calendar.addEvent(user.ADMIN, start, end, eventName, Visibility.BUSY, "");
		start = EseDateFormat.getInstance().parse("25.12.2011 18:00");
		end = EseDateFormat.getInstance().parse("25.12.2011 22:00");
		eventName = "test3 (xmas)";
		event3 = calendar.addEvent(user.ADMIN, start, end, eventName, Visibility.PUBLIC, "");
		
		start = EseDateFormat.getInstance().parse("1.1.2011 6:00");
		end = EseDateFormat.getInstance().parse("1.1.2011 7:00");
		eventName = "First sunrise of month";
		es = calendar.addEventSeries(user.ADMIN, start, end, eventName, 
				Visibility.BUSY, Repetition.MONTHLY, "gotta see that!");
		
	}
	
	@Test
	public void idShouldBeUnique() {
		assertTrue(event1.getId() != event2.getId());
		assertTrue(event2.getId() != event3.getId());
		assertTrue(event3.getId() != event1.getId());
		
		assertTrue(es.getId() != event3.getId());
	}
	
	@Test
	public void createdSerialEventsShouldHaveUniqueIds() {
		Iterator<CalendarEvent> iter = es.iterator(start);
		ArrayList<String> createdIds = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			CalendarEvent ce = iter.next();
			assertTrue(ce.getId().contains("-"));
			assertFalse(createdIds.contains(ce.getId()));
			createdIds.add(ce.getId());	
		}		
	}
	
	@Test
	public void testGetEventByIdNormal() {
		CalendarEvent e = calendar.getEventById(user.ADMIN, event1.getId());
		assertEquals(event1, e);
	}
	
	@Test
	public void testGetEventByIdSeries() {
		Iterator<CalendarEvent> iter = es.iterator(start);
		CalendarEvent e = calendar.getEventById(user.ADMIN, iter.next().getId());
		assertEquals(es, e.getSeries());
	}
	
	@Test
	public void deleteSingleInstanceOfSeries() {
		Iterator<CalendarEvent> iter = es.iterator(start);
		CalendarEvent e = iter.next();
		es.addExceptionalInstance(e.getId(), null);
		iter = es.iterator(start);
		e = iter.next();
		assertNull(e);
		e = iter.next();
		assertNotNull(e);
	}
	
}