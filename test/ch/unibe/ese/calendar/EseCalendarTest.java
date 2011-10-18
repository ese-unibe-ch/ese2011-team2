package ch.unibe.ese.calendar;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class EseCalendarTest {
	
	EseCalendar calendar;
	User user;
	
	@Before
	public void setup() {
		user = new User("dummy");
		calendar = new EseCalendar("TestCalendar", user);
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
		CalendarEvent event = initAnEvent();
		calendar.addEvent(user, event);
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
		CalendarEvent event = new CalendarEvent(sDate, eDate, "event", true);
		return event;
	}

}
