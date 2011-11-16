package ch.unibe.ese.calendar.util;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;


/**
 * An abstract superclass for classes implementing EseCalendar
 *
 */
public abstract class AbstractCalendar extends EseCalendar {


	@Override
	public SortedSet<CalendarEvent> getEventsAt(User user, Date day) {
		Date dayStart = DateUtils.getStartOfDay(day);
		Date dayEnd = new Date(dayStart.getTime()+24*60*60*1000);
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
		return result;
	}

}