package ch.unibe.ese.calendar;

import java.util.Comparator;

import controllers.Security;

public class ContactsComparator implements Comparator<User> {
	
	User connectedUser;
	
	/**
	 * The ContactsComparator will make sure that the connectedUser is 
	 * listed first and the other contacts sorted alphabetically.
	 */
	public ContactsComparator(User connectedUser) {
		this.connectedUser = connectedUser;
	}

	@Override
	public int compare(User user1, User user2) {
		if (user1.equals(connectedUser)) {
			return -1;
		}
		if (user2.equals(connectedUser)) {
			return 1;
		}
		return user1.getName().compareTo(user2.getName());
	}
	
}
