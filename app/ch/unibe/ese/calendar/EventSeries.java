package ch.unibe.ese.calendar;

import java.util.Date;

public class EventSeries extends CalendarEntry{
	
	public enum Repetition {DAILY, WEEKLY, MONTHLY}
	private Repetition repetition;
	
	public EventSeries(Date start, Date end, String name, boolean isPublic, String sRepetition) {
		super(start, end, name, isPublic);
		setRepetition(sRepetition);
	}



	public void setRepetition(String sRepetition) {
		if (sRepetition.equalsIgnoreCase("daily")) this.repetition = Repetition.DAILY;
		if (sRepetition.equalsIgnoreCase("weekly")) this.repetition = Repetition.WEEKLY;
		if (sRepetition.equalsIgnoreCase("monthly")) this.repetition = Repetition.MONTHLY;
	}

	public Repetition getRepetition(){
		return repetition;
	}
}