package controllers;

import javax.activity.InvalidActivityException;

import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Contacts extends Controller{
	
	public static void addToContacts(String name) {
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		User userToAdd = UserManager.getInstance().getUserByName(name);
		user.addToMyContacts(userToAdd);
		CalendarPage.calendar(name, null, null, -1);
	}
	
	/**
	 * Remove the user with the specified name from myContacts.
	 * @param name of the user to remove
	 */
	public static void removeFromContacts(String name) {
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		User userToRemove = UserManager.getInstance().getUserByName(name);
		try {
			user.removeFromMyContacts(userToRemove);
		} catch (InvalidActivityException e) {
			e.printStackTrace();
		}
		CalendarPage.calendar(name, null, null, -1);
	}
	
	/**
	 * First sets all Contacts to unselected then sets all Contacts that have 
	 * their name in checkedContacts[] to selected
	 * 
	 * @param checkedContacts: all Contacts who's checkbox is selected
	 */
	public static void includeContacts(String[] checkedContacts, String calendarName) {
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		user.unselectAllContacts();
		if (checkedContacts != null) {
			for (String uName: checkedContacts) {
				User u = UserManager.getInstance().getUserByName(uName);
				user.setContactSelection(u, true);
			}
		}	
		CalendarPage.calendar(userName, calendarName, null, -1);
	}
}
