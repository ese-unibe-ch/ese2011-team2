package ch.unibe.ese.calendar.security;

import java.security.Principal;

import ch.unibe.ese.calendar.User;

public class CalendarPrincipal implements Principal {

	private final User user;

	public CalendarPrincipal(User user) {
		if (user == null) {
			throw new IllegalArgumentException("user may not be null");
		}
		this.user = user;
	}

	@Override
	public String getName() {
		return user.getName();
	}

	public User getUser() {
		return user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		CalendarPrincipal other = (CalendarPrincipal) obj;
		return user.equals(other.user);
	}

	

}
