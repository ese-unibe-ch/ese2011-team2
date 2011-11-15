package ch.unibe.ese.calendar.impl;

import java.util.Date;

import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.Visibility;

/**
 * Hides the details of an event showing only the busy period
 */
class JustBusyEvent implements CalendarEvent {

	private CalendarEvent base;

	/**
	 * Create a JustBusy-Version of an event, that is an Event instance with hidden details.
	 * The JustBusy-Version has always the visibility busy.
	 * @param base
	 */
	public JustBusyEvent(CalendarEvent base) {
		this.base = base;
	}

	@Override
	public Visibility getVisibility() {
		return Visibility.BUSY;
	}

	@Override
	public Date getEnd() {
		return base.getEnd();
	}

	@Override
	public Date getStart() {
		return base.getStart();
	}

	@Override
	public String getName() {
		return "Busy";
	}

	@Override
	public EseCalendar getCalendar() {
		return base.getCalendar();
	}

	@Override
	public EventSeries getSeries() {
		return base.getSeries();
	}

	@Override
	public String getDescription() {
		return "None";
	}

	@Override
	public String getId() {
		return base.getId()+"-BUSY";
	}
	
	//TODO hashcode and equals

}
