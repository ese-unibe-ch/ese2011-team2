package ch.unibe.ese.calendar.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.EventSeries.Repetition;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.Visibility;
import ch.unibe.ese.calendar.exceptions.EventNotFoundException;

/**
 * 
 * A union of multiple calendar
 * 
 * Method calls changing the Calendar only affect one main-calendar, all other operation are delegated to 
 * all members of the union
 *
 */
public class UnionCalendar extends AbstractCalendar {

	private EseCalendar mainCalendar;
	private EseCalendar[] otherCalendars;

	/**
	 * 
	 * @param mainCalendar the calendar against which operation modifying the calendar are perfmormed
	 * @param otherCalendars the other calendars of union
	 */
	public UnionCalendar(EseCalendar mainCalendar, EseCalendar... otherCalendars) {
		this.mainCalendar = mainCalendar;
		this.otherCalendars = otherCalendars;
	}
	
	@Override
	public Iterator<CalendarEvent> iterate(User user, Date start) {
		List<Iterator<CalendarEvent>> baseIterators = new ArrayList<Iterator<CalendarEvent>>();
		baseIterators.add(mainCalendar.iterate(user, start));
		for(EseCalendar other: otherCalendars) {
			baseIterators.add(other.iterate(user, start));
		}
		return EventIteratorUtils.merge(baseIterators);
	}

	@Override
	public CalendarEvent getEventById(User user, String id) {
		for(EseCalendar other: otherCalendars) {
			try {
				return other.getEventById(user, id);
			} catch (EventNotFoundException e) {
				
			}
		}
		return mainCalendar.getEventById(user, id);
	}

	@Override
	public CalendarEvent removeEvent(User user, String id) {
		return mainCalendar.removeEvent(user, id);
	}

	@Override
	public EventSeries removeEventSeries(User user, String id) {
		return mainCalendar.removeEventSeries(user, id);
	}

	@Override
	public EventSeries addEventSeries(User user, Date start, Date end,
			String eventName, Visibility visibility, Repetition repetition,
			String description) {
		return mainCalendar.addEventSeries(user, start, end, eventName, visibility, repetition, description);
	}

	@Override
	public CalendarEvent addEvent(User user, Date start, Date end,
			String eventName, Visibility visibility, String description) {
		return mainCalendar.addEvent(user, start, end, eventName, visibility, description);
	}

	/**
	 * @return the owner of the main calendar
	 */
	@Override
	public User getOwner() {
		return mainCalendar.getOwner();
	}
	
	@Override
	public String getName() {
		return mainCalendar.getName();
	}

	@Override
	public String toString() {
		Set<String> baseNames = new HashSet<String>();
		baseNames.add(mainCalendar.getName());
		for(EseCalendar other: otherCalendars) {
			baseNames.add(other.getName());
		}
		return "Union of "+baseNames.toString();
	}

	@Override
	public void select(boolean select) {
		
	}

	@Override
	public boolean isSelected() {
		return true;
	}

}
