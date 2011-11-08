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
	private EventSeries esMonthly;
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
		esMonthly = calendar.addEventSeries(user.ADMIN, start, end, eventName, 
				Visibility.BUSY, Repetition.MONTHLY, "gotta see that!");
		
	}
	
	@Test
	public void idShouldBeUnique() {
		assertTrue(event1.getId() != event2.getId());
		assertTrue(event2.getId() != event3.getId());
		assertTrue(event3.getId() != event1.getId());
		
		assertTrue(esMonthly.getId() != event3.getId());
	}
	
	@Test
	public void createdSerialEventsShouldHaveUniqueIds() {
		Iterator<CalendarEvent> iter = esMonthly.iterator(start);
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
		Iterator<CalendarEvent> iter = esMonthly.iterator(start);
		CalendarEvent e = calendar.getEventById(user.ADMIN, iter.next().getId());
		assertEquals(esMonthly, e.getSeries());
	}
	
	@Test
	public void deleteSingleInstanceOfSeries() {
		Iterator<CalendarEvent> iter = esMonthly.iterator(start);
		CalendarEvent e = iter.next();
		String oldId = e.getId();
		esMonthly.addExceptionalInstance(e.getId(), null);
		iter = esMonthly.iterator(start);
		e = iter.next();
		//assertEquals(oldId, e.getId());
		assertNull(e);
		e = iter.next();
		assertNotNull(e);
	}
	
	@Test
	public void deleteSingleInstanceOfDailySeries() throws ParseException {
		Date start = EseDateFormat.getInstance().parse("1.1.2011 13:00");
		Date end = EseDateFormat.getInstance().parse("1.1.2011 14:00");
		String eventName = "Shuffling";
		EventSeries esDaily = calendar.addEventSeries(user.ADMIN, start, end, eventName, 
				Visibility.BUSY, Repetition.DAILY, "every day I'm shuffling!");
		//except this time:
		Date exception = EseDateFormat.getInstance().parse("20.2.2011 13:00");
		Iterator<CalendarEvent> iter = esDaily.iterator(exception);
		CalendarEvent exceptionalEvent = iter.next();
		esDaily.addExceptionalInstance(exceptionalEvent.getId(), null);
		Date oneDayBeforeException = EseDateFormat.getInstance().parse("19.2.2011 12:00");
		iter = esDaily.iterator(oneDayBeforeException);
		CalendarEvent e = iter.next();
		assertNotNull(e);
		assertFalse("They are not the same instance, so the Ids should be different"
				, exceptionalEvent.getId().equals(e.getId()));
		assertNull(iter.next());
		assertNotNull(iter.next());
	}
	
}