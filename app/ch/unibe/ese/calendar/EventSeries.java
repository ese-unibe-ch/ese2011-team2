package ch.unibe.ese.calendar;

import java.util.Date;

public class EventSeries extends CalendarEntry{
	
	public enum Repetition {DAILY, WEEKLY, YEARLY}
	private Repetition repetition;
	
	public EventSeries(Date start, Date end, String name, boolean isPublic, String sRepetition) {
		if (name==null || start==null || end==null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.start = start;
		this.end = end;
		this.isPublic = isPublic;
		setRepetition(sRepetition);
	}



	public void setRepetition(String sRepetition) {
		if (sRepetition.equalsIgnoreCase("daily")) this.repetition = Repetition.DAILY;
		if (sRepetition.equalsIgnoreCase("weekly")) this.repetition = Repetition.WEEKLY;
		if (sRepetition.equalsIgnoreCase("yearly")) this.repetition = Repetition.YEARLY;
	}

	public Repetition getRepetition(){
		return repetition;
	}
}