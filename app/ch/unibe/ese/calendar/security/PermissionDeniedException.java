package ch.unibe.ese.calendar.security;

public class PermissionDeniedException extends RuntimeException {

	public PermissionDeniedException(Permission permission) {
		super("Pemission denied: "+permission);
	}

}
