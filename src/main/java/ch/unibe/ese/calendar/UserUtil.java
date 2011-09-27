package ch.unibe.ese.calendar;

import java.util.Set;

import javax.security.auth.Subject;

import ch.unibe.ese.calendar.security.CalendarPrincipal;

public class UserUtil {

	public static User getUserForSubject(Subject subject) {
		Set<CalendarPrincipal> principals = subject.getPrincipals(CalendarPrincipal.class);
		if (principals.size() > 1) {
			throw new RuntimeException("having more than one CalendarPrincipals not currently supported");
		}
		if (principals.size() < 1) {
			throw new RuntimeException("Subject has no CalendarPrincipal");
		}
		return principals.iterator().next().getUser();
		
	}

}
