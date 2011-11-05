package ch.unibe.ese.calendar.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.EventSeries;
import ch.unibe.ese.calendar.Visibility;

class EventSeriesImpl extends CalendarEntry implements EventSeries {

	private static final Locale locale = new Locale(
					"de", "CH");

	
	private Map<String, CalendarEvent> exceptionalInstance = new HashMap<String, CalendarEvent>();
	private Repetition repetition;

	EventSeriesImpl(Date start, Date end, String name, Visibility visibility, 
			Repetition repetition, EseCalendar calendar, String description) {
		super(start, end, name, visibility, calendar, description);
		this.repetition = repetition;
	}


	/* (non-Javadoc)
	 * @see ch.unibe.ese.calendar.EventSeries#getRepetition()
	 */
	public Repetition getRepetition() {
		return repetition;
	}

	/* (non-Javadoc)
	 * @see ch.unibe.ese.calendar.EventSeries#iterator(java.util.Date)
	 */
	public Iterator<CalendarEvent> iterator(Date start) {
		Iterator<CalendarEvent> result = new DayMergingIterator(start);
		return result;

	}

	
	/**
	 * note that the caller is responsible to check if the date matches this series
	 * 
	 * @dayStart Start of the day we want to get a SerialEvent of
	 * @return 	A single instance of this event (of the type SerialEvent) with the 
	 * 			parameters this series is defined by.
	 */
	private SerialEvent getAsSerialEventForDay(Date dayStart) {
		java.util.Calendar jucDayStart = java.util.Calendar.getInstance(locale);
		java.util.Calendar jucEventStart = java.util.Calendar.getInstance(locale);
		java.util.Calendar jucEventEnd = java.util.Calendar.getInstance(locale);
		jucEventEnd.setTime(getEnd());
		jucEventStart.setTime(getStart());
		jucDayStart.setTime(dayStart);
		int year = jucDayStart.get(Calendar.YEAR);
		int month = jucDayStart.get(Calendar.MONTH);
		int day = jucDayStart.get(Calendar.DAY_OF_MONTH);
		
		long consecutiveNumber = getConsecutiveNumber(jucDayStart);
		
		int hour = jucEventStart.get(Calendar.HOUR_OF_DAY);
		int min = jucEventStart.get(Calendar.MINUTE);
		jucDayStart.set(year, month, day, hour, min);
		Date start = jucDayStart.getTime();
		
		int durDay = jucEventEnd.get(Calendar.DAY_OF_YEAR)-jucEventStart.get(Calendar.DAY_OF_YEAR);
		int durMonth = jucEventEnd.get(Calendar.MONTH)-jucEventStart.get(Calendar.MONTH);
		int durYear = jucEventEnd.get(Calendar.YEAR)-jucEventStart.get(Calendar.YEAR);
		int hour2 = jucEventEnd.get(Calendar.HOUR_OF_DAY);
		int min2 = jucEventEnd.get(Calendar.MINUTE);
		jucEventEnd.set(year+durYear, month + durMonth, day + durDay, hour2, min2);
		Date end = jucEventEnd.getTime();
		SerialEvent se = new SerialEvent(start, end, getName(), getVisibility(), 
				this, getCalendar(), getDescription(), consecutiveNumber);
		return se;
	}

	/**
	 * 
	 * @dayStart Start of the day we want to get a SerialEvent of
	 * @return
	 */
	private boolean dateMatches(Date dayStart) {
		Repetition repetition = getRepetition();
		java.util.Calendar jucProtoEventStart = java.util.Calendar.getInstance(locale);
		java.util.Calendar jucProtoEventEnd = java.util.Calendar.getInstance(locale);
		java.util.Calendar jucEvaluatedDay = java.util.Calendar.getInstance(locale);
		jucEvaluatedDay.setTime(dayStart);
		int weekDayOfDate = jucEvaluatedDay.get(Calendar.DAY_OF_WEEK);
		int monthDayOfDate = jucEvaluatedDay.get(Calendar.DAY_OF_MONTH);
		jucProtoEventStart.setTime(getStart());
		jucProtoEventEnd.setTime(getEnd());
		//FIXME change of year could lead to negative maxDur
		int maxDur = jucProtoEventEnd.get(Calendar.DAY_OF_YEAR) - jucProtoEventStart.get(Calendar.DAY_OF_YEAR);
		boolean match = false;
		for (int dur = 0; dur <= maxDur; dur++) {
			//FIXME doesn't work for events at end of week of of week (use Calendar.add instead)
			int weekDayOfEventSerie = jucProtoEventStart.get(Calendar.DAY_OF_WEEK) + dur;
			int monthDayOfEventSerie = jucProtoEventStart.get(Calendar.DAY_OF_MONTH) + dur;
			if (repetition.equals(repetition.DAILY)) {
				match = true;
			}
			if (repetition.equals(repetition.WEEKLY)) {
				if(weekDayOfEventSerie == weekDayOfDate) match = true;
			}
			if (repetition.equals(repetition.MONTHLY)) {
				if(monthDayOfEventSerie == monthDayOfDate) match = true;
			}
		}
		return match;
	}
	
	private long getConsecutiveNumber(Calendar jucEvaluatedDay) {
		java.util.Calendar jucProtoEventStart = java.util.Calendar.getInstance(locale);
		jucProtoEventStart.setTime(getStart());
		//TODO apply more efficient algorithm
		long i = 0;
		if (jucProtoEventStart.equals(jucEvaluatedDay)) {
			return 0;
		}
		if (jucProtoEventStart.before(jucEvaluatedDay)) {
			while (jucProtoEventStart.before(jucEvaluatedDay)) {
				i++;
				jucEvaluatedDay.add(Calendar.DAY_OF_MONTH, -1);
			}
		} else {
			while (jucProtoEventStart.after(jucEvaluatedDay)) {
				i--;
				jucEvaluatedDay.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		return i;
	}
	

	@Override
	public CalendarEvent getEventByConsecutiveNumber(long consecutiveNumber) {
		if (consecutiveNumber == 0)
			return getAsSerialEventForDay(getStart());
		throw new UnsupportedOperationException("not yet impl");
	}


	@Override
	public void addExceptionalInstance(String id,
			CalendarEvent exceptionalEvent) {
		exceptionalInstance.put(id, exceptionalEvent);
	}


	/**
	 * Help! what does this thing do?
	 *
	 */
	private class DayMergingIterator implements Iterator<CalendarEvent> {

		private Date currentDate;
		
		public DayMergingIterator(Date start) {
			currentDate = start;
		}

		/**
		 * @return true as a Series lasts forever
		 */
		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public CalendarEvent next() {
			while (!dateMatches(currentDate)) {
				currentDate = nextDay();
			}
			CalendarEvent result = getAsSerialEventForDay(currentDate);
			if (exceptionalInstance.containsKey(result.getId())) {
				result = exceptionalInstance.get(result.getId());
				System.out.println("Added exception: " + result);
			}
			currentDate = nextDay();
			return result;
		}

		private Date nextDay() {
			//TODO use calendar to take dlst into account
			return new Date(currentDate.getTime()+24*60*60*1000);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}