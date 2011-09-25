package ch.unibe.ese.calendar.security;

import java.security.BasicPermission;
import java.security.Permission;

/** 
 * Permission to write to a calendar
 * 
 * @author reto
 */
public class CalendarWritePermission extends BasicPermission {

	/**
	 * The actions parameter is ignored and only here to support serialization in the policy file format 
	 * 
	 * @param calendarName The name of the calendar
	 * @param actions ignored
	 */
	public CalendarWritePermission(String calendarName, String actions) {
		super(calendarName, actions);
	}

	@Override
	public boolean implies(Permission p) {
		// TODO Auto-generated method stub
		boolean result =  super.implies(p);
		return result;
	}	


}
