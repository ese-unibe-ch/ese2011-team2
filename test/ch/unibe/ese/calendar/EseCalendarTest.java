package ch.unibe.ese.calendar;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class EseCalendarTest {
	
	private EseCalendar calendar;
	private User user;
	private CalendarEvent event;
	private EventSeries eventSeries;
	
	@Before
	public void setup() {
		user = new User("dummy");
		calendar = new EseCalendar("TestCalendar", user);
		event = initAnEvent();
		eventSeries = initAnEventSeries();
	}
	
	@Test
	public void getName() {
		assertEquals("TestCalendar", calendar.getName());
	}
	
	@Test
	public void getOwner() {
		assertEquals(user, calendar.getOwner());
	}
	
	@Test
	public void addEvent() {
		assertTrue(calendar.getStartDateSortedSet().isEmpty());
		calendar.addEvent(user.ADMIN, event);
		assertFalse(calendar.getStartDateSortedSet().isEmpty());
		assertTrue(calendar.getStartDateSortedSet().contains(event));
	}
	
	@Test
	public void removeEvent() {
		calendar.addEvent(user.ADMIN, event);
		assertTrue(calendar.getStartDateSortedSet().contains(event));
		calendar.removeEvent(user.ADMIN, event.hashCode(), event.getStart());
		assertTrue(calendar.getStartDateSortedSet().isEmpty());
	}
	
	@Test
	public void addEventSeries() {
		assertTrue(calendar.getStartDateSortedSetOfSeries().isEmpty());
		calendar.addEventSeries(user.ADMIN, eventSeries);
		assertFalse(calendar.getStartDateSortedSetOfSeries().isEmpty());
		assertTrue(calendar.getStartDateSortedSetOfSeries().contains(eventSeries));
	}
	
	//HashCode of an event is not unique. We will need to refactor this sooner of later
	@Test
	public void getEventByHash() {
		calendar.addEvent(user.ADMIN, event);
		assertEquals(event, calendar.getEventByHash(user.ADMIN, event.hashCode(), event.getStart()));
	}
	
	@Test
	public void getEventsAtGivenDate() {
		calendar.addEvent(user.ADMIN, event);
		Date dayOnWichEventIsHappening = initADate();
		assertTrue(calendar.getEventsAt(user.ADMIN, dayOnWichEventIsHappening).contains(event));
	}

	private CalendarEvent initAnEvent() {
		SimpleDateFormat simple = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		Date sDate = null;
		Date eDate = null;
		try {
			sDate = simple.parse("12.11.11 20:00");
			eDate = simple.parse("12.11.11 22:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new CalendarEvent(sDate, eDate, "event", true);
	}
	
	private EventSeries initAnEventSeries() {
		SimpleDateFormat simple = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		Date sDate = null;
		Date eDate = null;
		try {
			sDate = simple.parse("23.12.11 15:00");
			eDate = simple.parse("13.12.11 16:30");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new EventSeries(sDate, eDate, "eventSeries", true, "weekly");
	}
	
	private Date initADate() {
		SimpleDateFormat simple = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		Date date = null;
		try {
			date = simple.parse("12.11.11 20:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
