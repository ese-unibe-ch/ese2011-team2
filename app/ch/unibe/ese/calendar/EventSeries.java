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
	 * @date Start of the day we want to get a SerialEvent of
	 * @return 	A single instance of this event (of the type SerialEvent) with the 
	 * 			parameters this series is defined by.
	 */
	private SerialEvent getAsSerialEventForDay(Date dayStart) {
		java.util.Calendar juc = java.util.Calendar.getInstance(new Locale(
				"de", "CH"));
		juc.setTime(dayStart);
		int year = juc.get(Calendar.YEAR);
		int month = juc.get(Calendar.MONTH);
		int day = juc.get(Calendar.DAY_OF_MONTH);
		juc.setTime(getStart());
		int hour = juc.get(Calendar.HOUR_OF_DAY);
		int min = juc.get(Calendar.MINUTE);
		juc.set(year, month, day, hour, min);
		Date start = juc.getTime();
		juc.setTime(getEnd());
		int hour2 = juc.get(Calendar.HOUR_OF_DAY);
		int min2 = juc.get(Calendar.MINUTE);
		juc.set(year, month, day, hour2, min2);
		Date end = juc.getTime();
		SerialEvent se = new SerialEvent(start, end, getName(), getVisibility().toString(), 
				this, getCalendar(), getDescription());
		return se;
	}

	private boolean dateMatches(Date date) {
		Repetition repetition = getRepetition();
		java.util.Calendar juc1 = java.util.Calendar.getInstance(new Locale(
				"de", "CH"));
		juc1.setTime(getStart());

		int weekDayOfEventSerie = juc1.get(Calendar.DAY_OF_WEEK);
		int monthDayOfEventSerie = juc1.get(Calendar.DAY_OF_MONTH);
		java.util.Calendar juc2 = java.util.Calendar.getInstance(new Locale(
				"de", "CH"));
		juc2.setTime(date);
		int weekDayOfDate = juc2.get(Calendar.DAY_OF_WEEK);
		int monthDayOfDate = juc2.get(Calendar.DAY_OF_MONTH);
		if (repetition.equals(repetition.DAILY)) {
			System.out.println("daily");
			return true;
		}
		if (repetition.equals(repetition.WEEKLY)) {
			System.out.println("weekly");
			System.out.println("weekDayOfEventSerie" + weekDayOfEventSerie);
			System.out.println("weekDayOfDate" + weekDayOfDate);
			return (weekDayOfEventSerie == weekDayOfDate);
		}
		if (repetition.equals(repetition.MONTHLY)) {
			System.out.println("monthly");
			return (monthDayOfEventSerie == monthDayOfDate);
		}
		return false;
	}
	
	public boolean isASerie(){
		return false;
	}

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