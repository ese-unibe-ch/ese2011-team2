package ch.unibe.ese.calendar;

import java.util.Date;

public class CalendarEvent {

	private boolean isPublic;
	private Date end;
	private Date start;
	private String name;

	public CalendarEvent(Date start, Date end, String name, boolean isPublic) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.isPublic = isPublic;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public Date getEnd() {
		return end;
	}

	public Date getStart() {
		return start;
	}

	public String getName() {
		return name;
	}

}
