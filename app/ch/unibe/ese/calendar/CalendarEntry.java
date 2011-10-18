package ch.unibe.ese.calendar;

import java.util.Date;

public class CalendarEntry {

	protected boolean isPublic;
	protected Date end;
	protected Date start;
	protected String name;

	public CalendarEntry() {
		super();
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

	public void set(String eventName, Date startDate, Date endDate, boolean isPublic) {
		this.name = eventName;
		this.start = startDate;
		this.end = endDate;
		this.isPublic = isPublic;
	}

	@Override
	public String toString() {
		return "CalendarEvent [isPublic=" + isPublic + ", end=" + end
				+ ", start=" + start + ", name=" + name + "]";
	}

}