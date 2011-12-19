package ch.unibe.ese.calendar.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.Visibility;
import ch.unibe.ese.calendar.util.EseDateFormat;

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
		Set<String> ids = new HashSet<String>();
		ids.add(event.getId());
		
		CalendarEvent event2 = calendar.addEvent(user.ADMIN, start, end, eventName, Visibility.PUBLIC,"random Kommentar1");
		assertTrue(ids.add(event2.getId()));
		CalendarEvent event3 = calendar.addEvent(user.ADMIN, start, end, eventName, Visibility.PUBLIC,"random Kommentar1");
		assertTrue(ids.add(event3.getId()));
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testIllegalArgumentsWhenCreatingEvent() {
		@SuppressWarnings("unused")
		CalendarEvent event = calendar.addEvent(user.ADMIN, null, null, null, null, "I'm an illeagl immigrant");
	}
}
