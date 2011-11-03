package ch.unibe.ese.calendar.impl;

import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.EventSeries.Repetition;
import ch.unibe.ese.calendar.util.EseDateFormat;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.Visibility;

public class EventTest extends UnitTest {

	private EseCalendar calendar;
	private User user;
	private Date start, end;
	private String eventName;
	
	@Before
	public void setup() throws ParseException {
		user = new User("dummy");
		calendar = new EseCalendarImpl("TestCalendar", user);
		start = EseDateFormat.getInstance().parse("12.11.11 20:00");
		end = EseDateFormat.getInstance().parse("12.11.11 22:00");
		eventName = "test";
	}
	
	@Test
	public void getIDs() {
		
		CalendarEvent event = calendar.addEvent(user.ADMIN, start, end, eventName, Visibility.PUBLIC,"random Kommentar1");
		long startId = event.getId();
		assertEquals(startId++, event.getId());
		
		CalendarEvent event2 = calendar.addEvent(user.ADMIN, start, end, eventName, Visibility.PUBLIC,"random Kommentar1");
		CalendarEvent event3 = calendar.addEvent(user.ADMIN, start, end, eventName, Visibility.PUBLIC,"random Kommentar1");
		assertEquals(startId++, event2.getId());
		assertEquals(startId++, event3.getId());
		EventSeries event4 = calendar.addEventSeries(user.ADMIN, start, end, eventName, Visibility.PUBLIC, Repetition.WEEKLY, "random Kommentar1");
		EventSeries event5 = calendar.addEventSeries(user.ADMIN, start, end, eventName, Visibility.PUBLIC, Repetition.WEEKLY, "random Kommentar1");
		assertEquals(startId++, event4.getId());
		assertEquals(startId++, event5.getId());
	}
}
