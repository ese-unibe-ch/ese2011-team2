package ch.unibe.ese.calendar.exceptions;

import ch.unibe.ese.calendar.User;

/**
 * Is thrown when a user attempts to remove his last remaining calendar.
 * A user is not allowed to have no calendars at all.
 */
@SuppressWarnings("serial")
public class CanNotRemoveLastCalendarException extends RuntimeException {

	public CanNotRemoveLastCalendarException(User user) {
		super(user.getName() + " may not remove his last calendar");
	}

}
