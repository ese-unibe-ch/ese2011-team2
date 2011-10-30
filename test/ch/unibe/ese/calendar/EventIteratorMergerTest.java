package ch.unibe.ese.calendar;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jaxen.function.StartsWithFunction;
import org.junit.Before;
import org.junit.Test;

public class EventIteratorMergerTest{
	java.util.Calendar juc = java.util.Calendar.getInstance();
	CalendarEvent calendarEvent1;
	{
		juc.set(2011, 11, 21, 10, 15);
		Date start = juc.getTime();
		juc.set(2011, 11, 21, 11, 15);
		Date end = juc.getTime();
		calendarEvent1 = new CalendarEvent(start, end,
				"event 1", true, null);
	}
	
	CalendarEvent calendarEvent2;
	{
		juc.set(2011, 11, 21, 10, 16);
		Date start = juc.getTime();
		juc.set(2011, 11, 21, 11, 15);
		Date end = juc.getTime();
		calendarEvent1 = new CalendarEvent(start, end,
				"event 1", true, null);
	}
	
	CalendarEvent calendarEvent3;
	{
		juc.set(2011, 11, 21, 10, 17);
		Date start = juc.getTime();
		juc.set(2011, 11, 21, 11, 15);
		Date end = juc.getTime();
		calendarEvent1 = new CalendarEvent(start, end,
				"event 1", true, null);
	}
	
	CalendarEvent calendarEvent4;
	{
		juc.set(2011, 11, 21, 10, 18);
		Date start = juc.getTime();
		juc.set(2011, 11, 21, 11, 15);
		Date end = juc.getTime();
		calendarEvent1 = new CalendarEvent(start, end,
				"event 1", true, null);
	}
	
	CalendarEvent calendarEvent5;
	{
		juc.set(2011, 11, 21, 10, 19);
		Date start = juc.getTime();
		juc.set(2011, 11, 21, 11, 15);
		Date end = juc.getTime();
		calendarEvent1 = new CalendarEvent(start, end,
				"event 1", true, null);
	}
	
	
	/** 
	 * tests merging an iterator with 2 and one with 3 events
	 */
	@Test
	public void merge3and2() {
		Set<CalendarEvent> set3 = new TreeSet<CalendarEvent>(new StartDateComparator());
		set3.add(calendarEvent4);
		set3.add(calendarEvent1);
		set3.add(calendarEvent3);
		Set<CalendarEvent> set2 = new TreeSet<CalendarEvent>(new StartDateComparator());
		set2.add(calendarEvent2);
		set2.add(calendarEvent5);
		Iterator<CalendarEvent> merged = new EventIteratorMerger(set3.iterator(), set2.iterator());
		assertTrue(merged.hasNext());
		assertEquals(calendarEvent1, merged.next());
		assertTrue(merged.hasNext());
		assertEquals(calendarEvent2, merged.next());
		assertTrue(merged.hasNext());
		assertEquals(calendarEvent3, merged.next());
		assertTrue(merged.hasNext());
		assertEquals(calendarEvent4, merged.next());
		assertTrue(merged.hasNext());
		assertEquals(calendarEvent5, merged.next());
		assertFalse(merged.hasNext());
	}
	
}
