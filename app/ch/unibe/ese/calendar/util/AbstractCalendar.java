package ch.unibe.ese.calendar.util;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;


/**
 * An abstract superclass for classes implementing EseCalendar
 *
 */
public abstract class AbstractCalendar extends EseCalendar {

	private final long ONE_DAY = 24*60*60*1000;
	private final long ONE_YEAR = 365*ONE_DAY;

	@Override
	public SortedSet<CalendarEvent> getEventsAt(User user, Date day) {
		Date dayStart = DateUtils.getStartOfDay(day);
		Date dayEnd = new Date(dayStart.getTime()+ONE_DAY);
		//TODO it seems quite pointless to re-sort the events that are already sorted in the iterator
		SortedSet<CalendarEvent> result = new TreeSet<CalendarEvent>(new StartDateComparator());
		Iterator<CalendarEvent> iter = iterate(user, dayStart);
		while (iter.hasNext()) {
			CalendarEvent ce = iter.next();		
			if (ce.getStart().after(dayEnd)) {
				break;
			}
			result.add(ce);
				
		}
		return Collections.unmodifiableSortedSet(result);
	}

	@Override
	public SortedSet<CalendarEvent> getEventsByRegex(User user, String regex) {
		Date day = new Date();
		Date dayStart = new Date(day.getTime()-ONE_YEAR);
		Date dayEnd = new Date(day.getTime()+ONE_YEAR);
		SortedSet<CalendarEvent> result = new TreeSet<CalendarEvent>(new StartDateComparator());
		Iterator<CalendarEvent> iter = iterate(user, dayStart);
		while (iter.hasNext()) {
			CalendarEvent ce = iter.next();
			if (ce.getStart().after(dayEnd)) {
				break;
			}
			if (!Pattern.matches(regex, ce.getName()) &&
			    !Pattern.matches(regex, ce.getDescription())) {
				continue;
			}
			result.add(ce);
		}
		return Collections.unmodifiableSortedSet(result);
	}

}
