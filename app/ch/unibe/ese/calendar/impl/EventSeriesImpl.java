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
	//private int instanceDurationInDays;
	private final int duration;

	EventSeriesImpl(Date start, Date end, String name, Visibility visibility, 
			Repetition repetition, EseCalendar calendar, String description) {
		super(start, end, name, visibility, calendar, description);
		this.repetition = repetition;
		duration = (int) (getEnd().getTime() - getStart().getTime());
	}


	private void setToStartOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
	}


	public Repetition getRepetition() {
		return repetition;
	}


	public Iterator<CalendarEvent> iterator(Date start) {
		return startingEventIterator(new Date(start.getTime()));

	}
	
	private Iterator<CalendarEvent> startingEventIterator(Date start) {
		Calendar jucStart = java.util.Calendar.getInstance(locale);
		jucStart.setTime(start);
		setToStartOfDay(jucStart);
		while (!dateMatches(jucStart.getTime())) {
				jucStart.add(Calendar.DAY_OF_MONTH, 1);
		}
		long firstConsecutiveNumber = getConsecutiveNumber(jucStart.getTime());
		Iterator<CalendarEvent> result = new EventsIterator(firstConsecutiveNumber);
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
		
		long consecutiveNumber = getConsecutiveNumber(jucDayStart.getTime());
		
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
	 * @return true if an instance of this series starts at the specified date
	 */
	private boolean dateMatches(Date dayStart) {
		Repetition repetition = getRepetition();
		java.util.Calendar jucProtoEventStart = java.util.Calendar.getInstance(locale);
		java.util.Calendar jucEvaluatedDay = java.util.Calendar.getInstance(locale);
		jucEvaluatedDay.setTime(dayStart);
		jucProtoEventStart.setTime(getStart());
		if (repetition.equals(repetition.DAILY)) {
			return true;
		}
		if (repetition.equals(repetition.WEEKLY)) {
			int weekDayOfEventSerie = jucProtoEventStart.get(Calendar.DAY_OF_WEEK);
			int weekDayOfDate = jucEvaluatedDay.get(Calendar.DAY_OF_WEEK);
			if(weekDayOfEventSerie == weekDayOfDate) {
				return true;
			}
		}
		if (repetition.equals(repetition.MONTHLY)) {
			int monthDayOfEventSerie = jucProtoEventStart.get(Calendar.DAY_OF_MONTH);
			int monthDayOfDate = jucEvaluatedDay.get(Calendar.DAY_OF_MONTH);
			if(monthDayOfEventSerie == monthDayOfDate) { 
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @param date
	 * @return the consecutive number of an iteration at the day of date
	 */
	private long getConsecutiveNumber(Date date) {
		Calendar startOfEvaluatedDay = java.util.Calendar.getInstance(locale);
		startOfEvaluatedDay.setTime(date);
		setToStartOfDay(startOfEvaluatedDay);
		java.util.Calendar jucProtoEventStart = java.util.Calendar.getInstance(locale);
		jucProtoEventStart.setTime(getStart());
		setToStartOfDay(jucProtoEventStart);
		//TODO apply more efficient algorithm
		long i = 0;
		if (jucProtoEventStart.equals(startOfEvaluatedDay)) {
			return 0;
		}
		if (jucProtoEventStart.before(startOfEvaluatedDay)) {
			while (jucProtoEventStart.before(startOfEvaluatedDay)) {
				i++;
				increaseCalendarByIterations(startOfEvaluatedDay,-1);
			}
		} else {
			while (jucProtoEventStart.after(startOfEvaluatedDay)) {
				i--;
				increaseCalendarByIterations(startOfEvaluatedDay,1);
			}
		}
		return i;
	}
	

	private void increaseCalendarByIterations(Calendar calendar,
			int amount) {
		if (repetition.equals(Repetition.DAILY)) {
			calendar.add(Calendar.DAY_OF_MONTH, amount);
		}
		if (repetition.equals(Repetition.WEEKLY)) {
			calendar.add(Calendar.WEEK_OF_YEAR, amount);
		}
		if (repetition.equals(Repetition.MONTHLY)) {
			calendar.add(Calendar.MONTH, amount);
		}
	}


	@Override
	public CalendarEvent getEventByConsecutiveNumber(long consecutiveNumber) {
		if (exceptionalInstance.containsKey(this.getId()+"-" + consecutiveNumber))
			return exceptionalInstance.get(this.getId()+"-" + consecutiveNumber);
		java.util.Calendar jucEventStart = java.util.Calendar.getInstance(locale);
		jucEventStart.setTime(getStart());
		java.util.Calendar jucEventEnd = java.util.Calendar.getInstance(locale);
		jucEventEnd.setTime(getEnd());
		if (repetition.equals(Repetition.DAILY)) {
			jucEventStart.add(Calendar.DAY_OF_MONTH, (int) consecutiveNumber);
			jucEventEnd.add(Calendar.DAY_OF_MONTH, (int) consecutiveNumber);
		}
		if (repetition.equals(Repetition.WEEKLY)) {
			jucEventStart.add(Calendar.WEEK_OF_YEAR, (int) consecutiveNumber);
			jucEventEnd.add(Calendar.WEEK_OF_YEAR, (int) consecutiveNumber);
		}
		if (repetition.equals(Repetition.MONTHLY)) {
			jucEventStart.add(Calendar.MONTH, (int) consecutiveNumber);
			jucEventEnd.add(Calendar.MONTH, (int) consecutiveNumber);
		}
		
		SerialEvent se = new SerialEvent(jucEventStart.getTime(), jucEventEnd.getTime(), getName(), getVisibility(), 
				this, getCalendar(), getDescription(), consecutiveNumber);
		return se;
	}


	@Override
	public void addExceptionalInstance(String id,
			CalendarEvent exceptionalEvent) {
		exceptionalInstance.put(id, exceptionalEvent);
	}


	private class EventsIterator implements Iterator<CalendarEvent> {

		private long currentConsecutiveNumber;
		
		public EventsIterator(long firstConsecutiveNumber) {
			currentConsecutiveNumber = firstConsecutiveNumber;
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
			CalendarEvent nextEvent = getEventByConsecutiveNumber(currentConsecutiveNumber++);
			if(nextEvent != null) 
				return nextEvent;
			else return next();
		}


		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}