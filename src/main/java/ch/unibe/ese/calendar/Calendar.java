package ch.unibe.ese.calendar;

import java.security.AccessController;

import ch.unibe.ese.calendar.security.CalendarWritePermission;

public class Calendar {
	
	protected Calendar(String name, User owner) {
		this.name = name;
		this.owner = owner;
	}

	private String name;
	private User owner;
	
	
	
	public String getName() {
		return name;
	}
	
	public User getOwner() {
		return owner;
	}

	public void addEvent(CalendarEvent calendarEvent) {
		AccessController.checkPermission(new CalendarWritePermission(name, ""));
		// TODO Auto-generated method stub
		
	}

}
