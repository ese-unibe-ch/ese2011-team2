package ch.unibe.ese.calendar.security;


import java.util.HashSet;
import java.util.Set;

import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.User;

/**
 * When this policy is in place a subjects with a CalendarPrincipal (i.e. the Subject returned by 
 * <code>User.getSubject</code> has privileged calendar access to the calendar they own.
 */
public class Policy {

	private static Policy instance = new Policy();

	private Policy() {
		
	}

	public boolean hasPermission(User user, Permission permission) {
		if (User.ADMIN.equals(user)) {
			return true;
		}
		return getUserPermissions(user).contains(permission);
	}

	private Set<Permission> getUserPermissions(User user) {
		Set<Permission> result = new HashSet<Permission>();
		Set<EseCalendar> userCalendars = CalendarManager.getInstance().getCalendarsOf(user);
		for (EseCalendar cal : userCalendars) {
			result.add(new PrivilegedCalendarAccessPermission(cal.getName()));
		}
		return result;
	}
	
	public static Policy getInstance() {
		return instance;
	}

	public void checkPermission(User user,
			Permission permission) {
		if (!hasPermission(user, permission))	{
			throw new PermissionDeniedException(permission);
		}
		
	}

}
