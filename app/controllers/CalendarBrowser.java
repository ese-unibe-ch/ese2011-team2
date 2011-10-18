package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.CellEditor;

import controllers.CalendarBrowser.Day;

import ch.unibe.ese.calendar.CalendarEntry;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;

public class CalendarBrowser {

	private final Locale locale;

	public class Day {

		private Calendar juc;

		public Day(Calendar juc) {
			juc.set(Calendar.HOUR_OF_DAY,0);
			juc.set(Calendar.MINUTE,0);
			juc.set(Calendar.SECOND,0);
			juc.set(Calendar.MILLISECOND,0);
			this.juc = juc;
		}

		public int getDayOfMonth() {
			return juc.get(Calendar.DAY_OF_MONTH);
		}
		
		public int getMonth() {
			return juc.get(Calendar.MONTH);
		}
		
		public int getYear() {
			return juc.get(Calendar.YEAR);
		}

		public boolean getHasPublicEvents() {
			for (CalendarEntry event: calendar.getEventsAt(user, asCalendar().getTime())) {
				if (event.isPublic()) {
					return true;
				}
			}
			return false;
		}

		public boolean getHasPrivateEvents() {
			for (CalendarEntry event: calendar.getEventsAt(user, asCalendar().getTime())) {
				if (!event.isPublic()) {
					return true;
				}
			}
			return false;
		}
		
		public Calendar asCalendar() {
			return (Calendar) juc.clone();
		}
		
		public boolean isToday() {
			long millisSinceBeginOfDay = System.currentTimeMillis() - juc.getTimeInMillis()-month; 
			return (millisSinceBeginOfDay >= 0) && (millisSinceBeginOfDay < (24*60*60*1000)); 
		}
		
		public boolean isSelected() {
			return (selectedDay == juc.get(Calendar.DAY_OF_MONTH)) && (month == juc.get(Calendar.MONTH)) && (year == juc.get(Calendar.YEAR));
		}

		
	}

	private EseCalendar calendar;
	private int month;
	private int year;
	private int selectedDay;
	private User user;

	public CalendarBrowser(User user, EseCalendar calendar, int selectedDay, int month, int year, Locale locale) {
		this.user = user;
		this.calendar = calendar;
		this.month = month;
		this.year = year;
		this.locale = locale;
		this.selectedDay = selectedDay;
	}
	
	public String getMonthLabel() {
		Calendar juc = Calendar.getInstance(locale);
		juc.set(Calendar.MONTH, month);
		return juc.getDisplayName(Calendar.MONTH, Calendar.LONG, locale)+" "+year;
	}
	
	public String[] getWeekDaysLabels() {
		String[] result= new String[7];
		Calendar juc = Calendar.getInstance(locale);
		juc.set(Calendar.DAY_OF_WEEK, 1);
		for (int i = 0; i < 7; i++) {
			result[i] = juc.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
			juc.add(Calendar.DAY_OF_MONTH, 1);
		}
		return result;
	}
	
	/**
	 * Get the weeks of the current month, i.e. the weeks with at least one day within the current month.
	 * 
	 * @return an array of weeks (in turn arrays of days)
	 */
	public Day[][] getWeeks() {	
		List<Day[]> resultList = new ArrayList<Day[]>();
		for (int i = 0; i < 7; i++) {
			Day[] week = getWeek(i);
			if ((i > 3) && (week[0].asCalendar().get(Calendar.MONTH) != month)) {
				break;
			}
			resultList.add(week);
		}
		return resultList.toArray(new Day[0][0]);
		
	}
	
	/**
	 * Get a week relatively to this instances month,
	 * week 0 being the first week with 1 or more days withi the month
	 * 
	 * @return an array of 7 days representing the week
	 */
	public Day[] getWeek(int relativeWeekNumber) {
		//0 for now
		Day[] result = new Day[7];
		Calendar juc = Calendar.getInstance();
		juc.set(Calendar.MONTH, month);
		juc.set(Calendar.YEAR, year);
		juc.set(Calendar.DAY_OF_MONTH, relativeWeekNumber*7+1);
		int dayOfWeek = juc.get(Calendar.DAY_OF_WEEK);
		System.out.println(juc.get(Calendar.DAY_OF_WEEK));
		juc.add(Calendar.DAY_OF_MONTH, 1 - dayOfWeek);
		System.out.println(juc.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale));
		for (int i = 0; i < 7; i++) {
			result[i] = new Day((Calendar) juc.clone());
			juc.add(Calendar.DAY_OF_MONTH, 1);
		}
		return result ;
	}


	public EseCalendar getCalendar() {
		return calendar;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getPreviousMonth() {
		return month > 0? month-1 : 11;
	}
	
	public int getPreviousMonthYear() {
		return month > 0? year : year-1;
	}
	
	public int getNextMonth() {
		return month < 11? month+1 : 0;
	}
	
	public int getNextMonthYear() {
		return month < 11 ? year : year+1;
	}

}
