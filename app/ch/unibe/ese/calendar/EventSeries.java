package ch.unibe.ese.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

public class EventSeries extends CalendarEntry {

	public enum Repetition {
		DAILY, WEEKLY, MONTHLY
	}

	private Repetition repetition;

	public EventSeries(Date start, Date end, String name, String visibility, 
			String sRepetition, EseCalendar calendar, String description) {
		super(start, end, name, visibility, calendar, description);
		setRepetition(sRepetition);
	}

	public void setRepetition(String sRepetition) {
		if (sRepetition.equalsIgnoreCase("daily"))
			this.repetition = Repetition.DAILY;
		if (sRepetition.equalsIgnoreCase("weekly"))
			this.repetition = Repetition.WEEKLY;
		if (sRepetition.equalsIgnoreCase("monthly"))
			this.repetition = Repetition.MONTHLY;
	}

	public Repetition getRepetition() {
		return repetition;
	}

	public Iterator<CalendarEvent> iterator(Date start) {
		Iterator<CalendarEvent> result = new DayMergingIterator(start);
		return result;

	}

	/**
	 * 
	 * @param user
	 *            the user requesting the event
	 * @param date
	 *            a point in time that is part of the day for which the vents
	 *            are requested
	 * @return a sorted list of SerialEventS for the specified day
	 */
	public SortedSet<CalendarEvent> getSerialEventsForDay(User user, Date date) {
		SortedSet<CalendarEvent> result = new TreeSet<CalendarEvent>(
				new StartDateComparator());
		if (dateMatches(date)) {
			result.add(getAsSerialEventForDay(date));
		}
		return result;
	}
	
	/**
	 * note that the caller is response to check if the date matches this series
	 * 
	 * @dayStart Start of the day we want to get a SerialEvent of
	 * @return 	A single instance of this event (of the type SerialEvent) with the 
	 * 			parameters this series is defined by.
	 */
	private SerialEvent getAsSerialEventForDay(Date dayStart) {
		java.util.Calendar jucDayStart = java.util.Calendar.getInstance(new Locale(
				"de", "CH"));
		java.util.Calendar jucEventStart = java.util.Calendar.getInstance(new Locale(
				"de", "CH"));
		java.util.Calendar jucEventEnd = java.util.Calendar.getInstance(new Locale(
				"de", "CH"));
		jucEventEnd.setTime(getEnd());
		jucEventStart.setTime(getStart());
		jucDayStart.setTime(dayStart);
		int year = jucDayStart.get(Calendar.YEAR);
		int month = jucDayStart.get(Calendar.MONTH);
		int day = jucDayStart.get(Calendar.DAY_OF_MONTH);
		
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
		SerialEvent se = new SerialEvent(start, end, getName(), getVisibility().toString(), 
				this, getCalendar(), getDescription());
		return se;
	}

	/**
	 * 
	 * @dayStart Start of the day we want to get a SerialEvent of
	 * @return
	 */
	private boolean dateMatches(Date dayStart) {
		Repetition repetition = getRepetition();
		java.util.Calendar juc1 = java.util.Calendar.getInstance(new Locale(
				"de", "CH"));
		java.util.Calendar juc3 = java.util.Calendar.getInstance(new Locale(
				"de", "CH"));
		java.util.Calendar juc2 = java.util.Calendar.getInstance(new Locale(
				"de", "CH"));
		juc2.setTime(dayStart);
		int weekDayOfDate = juc2.get(Calendar.DAY_OF_WEEK);
		int monthDayOfDate = juc2.get(Calendar.DAY_OF_MONTH);
		juc1.setTime(getStart());
		juc3.setTime(getEnd());
		int maxDur = juc3.get(Calendar.DAY_OF_YEAR) - juc1.get(Calendar.DAY_OF_YEAR);
		System.out.println(maxDur);
		for (int dur = 0; dur <= maxDur+1; dur++) {
			int weekDayOfEventSerie = juc1.get(Calendar.DAY_OF_WEEK) + dur;
			int monthDayOfEventSerie = juc1.get(Calendar.DAY_OF_MONTH) + dur;
			System.out.println ("I'm going to  bc: " + dur);
			//System.out.println("we were here" + weekDayOfEventSerie);
			if (dur==1)
				throw new RuntimeException("weeeha, it works");
			
			if (repetition.equals(repetition.DAILY)) {
				return true;
			}
			if (repetition.equals(repetition.WEEKLY)) {
				return (weekDayOfEventSerie == weekDayOfDate);
			}
			if (repetition.equals(repetition.MONTHLY)) {
				return (monthDayOfEventSerie == monthDayOfDate);
			}
		}
		System.out.println("returning false");
		return false;
	}
	
	public boolean isASerie(){
		return false;
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