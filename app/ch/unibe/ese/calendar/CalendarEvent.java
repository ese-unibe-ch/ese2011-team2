package ch.unibe.ese.calendar;

import java.util.Date;

public class CalendarEvent {

	private boolean isPublic;
	private Date end;
	private Date start;
	private String name;

	public CalendarEvent(Date start, Date end, String name, boolean isPublic) {
		if (name==null || start==null || end==null) {
			throw new IllegalArgumentException();
		}
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

	public void set(String eventName, Date startDate, Date endDate,
			boolean isPublic) {
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
