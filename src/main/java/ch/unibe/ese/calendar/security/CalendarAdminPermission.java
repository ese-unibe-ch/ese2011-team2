package ch.unibe.ese.calendar.security;

import java.security.BasicPermission;

/** 
 * Permission to do calendar administration tasks (like purgin the calendar manager)
 * 
 * @author reto
 */
public class CalendarAdminPermission extends BasicPermission {

	public CalendarAdminPermission() {
		super("Calendar Admin", null);
	}	


}
