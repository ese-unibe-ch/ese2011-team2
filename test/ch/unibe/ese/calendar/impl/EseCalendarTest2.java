package ch.unibe.ese.calendar.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.EventSeries.Repetition;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.Visibility;
import ch.unibe.ese.calendar.util.EseDateFormat;

public class EseCalendarTest2 extends UnitTest {
	
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
		start = EseDateFormat.getInstance().parse("5.1.2010 18:00");
		end = EseDateFormat.getInstance().parse("12.3.2010 22:00");
		eventName = "test3: very long Event";
		event3 = calendar.addEvent(user.ADMIN, start, end, eventName, Visibility.PUBLIC, "");
		
		start = EseDateFormat.getInstance().parse("1.1.2011 6:00");
		end = EseDateFormat.getInstance().parse("10.1.2011 7:00");
		eventName = "Series of long Events";
		es = calendar.addEventSeries(user.ADMIN, start, end, eventName, 
				Visibility.BUSY, Repetition.MONTHLY, "gotta see that!");
		
	}
	
	@Test
	public void simpleCheck() throws ParseException {
		Date dayStart = EseDateFormat.getInstance().parse("12.11.2011 00:00");
		Iterator<CalendarEvent> eventsAt = calendar.getEventsAt(user.ADMIN, dayStart).iterator();
		assertEquals(event1, eventsAt.next());
		assertFalse(eventsAt.hasNext());		
	}
	
	@Test
	public void longNormalEventShouldAppear() throws ParseException {
		Date dayStart = EseDateFormat.getInstance().parse("12.1.2010 00:00");
		Iterator<CalendarEvent> eventsAt = calendar.getEventsAt(user.ADMIN, dayStart).iterator();
		assertEquals(event3, eventsAt.next());
		assertEquals(event2, eventsAt.next());
		assertFalse(eventsAt.hasNext());		
	}
	
	@Test
	public void longEventSeriesShouldAppear() throws ParseException {
		Date dayStart = EseDateFormat.getInstance().parse("4.1.2010 00:00");
		Iterator<CalendarEvent> eventsAt = calendar.getEventsAt(user.ADMIN, dayStart).iterator();
		assertEquals(es, eventsAt.next().getSeries());
		assertFalse(eventsAt.hasNext());		
	}
	
	@Test
	public void longSerialEventShouldHaveSameAttributesThroughWholeInstance() throws ParseException {
		Date dayStart = EseDateFormat.getInstance().parse("4.1.2010 00:00");
		CalendarEvent dayFourOfEvent = calendar.getEventsAt(user.ADMIN, dayStart).first();
		dayStart = EseDateFormat.getInstance().parse("1.1.2010 00:00");
		CalendarEvent dayOneOfEvent = calendar.getEventsAt(user.ADMIN, dayStart).first();
		assertEquals(dayOneOfEvent.getName(), dayFourOfEvent.getName());
		//do we want that?
		//assertEquals(dayOneOfEvent.getId(), dayFourOfEvent.getId()); 
		assertEquals(dayOneOfEvent.getCalendar(), dayFourOfEvent.getCalendar());
		assertEquals(dayOneOfEvent.getVisibility(), dayFourOfEvent.getVisibility());
		assertEquals(dayOneOfEvent.getSeries(), dayFourOfEvent.getSeries());
		assertEquals(dayOneOfEvent.getDescription(), dayFourOfEvent.getDescription());
		assertEquals(dayOneOfEvent.getStart(), dayFourOfEvent.getStart());
		assertEquals(dayOneOfEvent.getEnd(), dayFourOfEvent.getEnd());
		assertEquals(dayOneOfEvent.toString(), dayFourOfEvent.toString());
	}
}
