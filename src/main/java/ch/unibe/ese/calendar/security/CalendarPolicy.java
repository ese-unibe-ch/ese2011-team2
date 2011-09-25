package ch.unibe.ese.calendar.security;

import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.util.Set;

import ch.unibe.ese.calendar.Calendar;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.User;

public class CalendarPolicy extends Policy {
	
	private Policy originalPolicy;

	public CalendarPolicy() {
		this.originalPolicy = Policy.getPolicy();
	}

	@Override
	public PermissionCollection getPermissions(ProtectionDomain domain) {
		final PermissionCollection result = new Permissions();
		Principal[] principals = domain.getPrincipals();
		boolean hasPrincipal = false;
		for (Principal p : principals) {
			if (p instanceof CalendarPrincipal) {
				hasPrincipal = true;
				User user = ((CalendarPrincipal) p).getUser();
				//assign permssions for all owned calendars
				Set<Calendar> userCalendars = CalendarManager.getInstance().getCalendarsOf(user);
				for (Calendar cal : userCalendars) {
					result.add(new CalendarWritePermission(cal.getName(), null));
				}
			}
		}
		if (!hasPrincipal) {
			return originalPolicy.getPermissions(domain);
		}
		return result;
	}

}
