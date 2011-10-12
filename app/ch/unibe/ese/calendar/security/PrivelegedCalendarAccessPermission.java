package ch.unibe.ese.calendar.security;


/** 
 * Permission to read non-public events from and to write to a calendar
 * 
 */
@SuppressWarnings("serial")
public class PrivelegedCalendarAccessPermission extends Permission {

	private String calendarName;
	
	/**
	 * Permission for priviledged access to a calendar
	 * 
	 * @param calendarName The name of the calendar
	 */
	public PrivelegedCalendarAccessPermission(String calendarName) {
		this.calendarName = calendarName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((calendarName == null) ? 0 : calendarName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrivelegedCalendarAccessPermission other = (PrivelegedCalendarAccessPermission) obj;
		if (calendarName == null) {
			if (other.calendarName != null)
				return false;
		} else if (!calendarName.equals(other.calendarName))
			return false;
		return true;
	}


}
