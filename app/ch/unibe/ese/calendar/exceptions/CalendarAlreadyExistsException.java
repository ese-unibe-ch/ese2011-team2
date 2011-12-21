package ch.unibe.ese.calendar.exceptions;

@SuppressWarnings("serial")
public class CalendarAlreadyExistsException extends RuntimeException {

	public CalendarAlreadyExistsException(String reason) {
		super(reason);
	}

}
