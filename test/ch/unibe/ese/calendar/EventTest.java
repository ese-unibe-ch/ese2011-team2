package ch.unibe.ese.calendar;

import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import ch.unibe.ese.calendar.EventSeries.Repetition;

import play.test.UnitTest;

public class EventTest extends UnitTest {

	private EseCalendar calendar;
	private User user;
	private Date start, end;
	private String eventName;
	
	@Before
	public void setup() throws ParseException {
		user = new User("dummy");
		calendar = new EseCalendar("TestCalendar", user);
		start = EseDateFormat.getInstance().parse("12.11.11 20:00");
		end = EseDateFormat.getInstance().parse("12.11.11 22:00");
		eventName = "test";
	}
	
	@Test
	public void getIDs() {
		CalendarEvent event = calendar.addEvent(user.ADMIN, start, end, eventName, Visibility.PUBLIC,"random Kommentar1");
		assertEquals(6, event.getId());
		CalendarEvent event2 = calendar.addEvent(user.ADMIN, start, end, eventName, Visibility.PUBLIC,"random Kommentar1");
		CalendarEvent event3 = calendar.addEvent(user.ADMIN, start, end, eventName, Visibility.PUBLIC,"random Kommentar1");
		assertEquals(7, event2.getId());
		assertEquals(8, event3.getId());
		EventSeries event4 = calendar.addEventSeries(user.ADMIN, start, end, eventName, Visibility.PUBLIC, Repetition.WEEKLY, "random Kommentar1");
		EventSeries event5 = calendar.addEventSeries(user.ADMIN, start, end, eventName, Visibility.PUBLIC, Repetition.WEEKLY, "random Kommentar1");
		assertEquals(9, event4.getId());
		assertEquals(10, event5.getId());
	}
}
