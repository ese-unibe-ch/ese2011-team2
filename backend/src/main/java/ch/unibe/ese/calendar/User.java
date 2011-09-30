package ch.unibe.ese.calendar;

import javax.security.auth.Subject;

import ch.unibe.ese.calendar.security.CalendarPrincipal;

/**
 * Represents a User. Users have a unique names identifying them.
 * 
 *
 */
public class User {

	private String userName;

	public User(String userName) {
		this.userName = userName;
	}

	public Subject getSubject() {
		Subject subject = new Subject();
		CalendarPrincipal calendarPrincipal = new CalendarPrincipal(this);
		subject.getPrincipals().add(calendarPrincipal);
		return subject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		User other = (User) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + "]";
	}

	public String getName() {
		return userName;
	}

}
