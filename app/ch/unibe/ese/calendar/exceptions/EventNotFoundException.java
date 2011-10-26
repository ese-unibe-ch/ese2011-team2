package ch.unibe.ese.calendar.exceptions;

public class EventNotFoundException extends RuntimeException {
	
	public EventNotFoundException() {
		super();
	}

	public EventNotFoundException(String reason) {
		super(reason);
	}
}
