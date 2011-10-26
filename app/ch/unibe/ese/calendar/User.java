package ch.unibe.ese.calendar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Represents a User. Users have a unique names identifying them and
 * a Map myContacts, to which a user connected to a boolean can be added.
 *
 */
public class User {
	
	public static final User ADMIN = new User("admin");

	private String userName;
	private Object password;
	private Map<User, Boolean> myContacts = new HashMap<User, Boolean>();

	/**
	 * creates a user with the specified username and password
	 * @param userName
	 * @param password
	 */
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	/**
	 * creates a user with the specified username and a random password
	 * @param userName
	 */
	public User(String userName) {
		this(userName, Integer.toString((int)(Math.random()*1000)));
	}
	
	/**
	 * Adds a user to the Map myContacts. By default, they are
	 * not selected. 
	 * 
	 * If the user is already in the list, he is still only once in the map
	 * and selected is set to false.
	 * 
	 * @param userToAdd The user you want to add
	 */
	public void addToMyContacts(User userToAdd){
		myContacts.put(userToAdd, false);
	}
	
	/**
	 * Removs a user from the Map myContacts.
	 * There is no return value.
	 * @param userToRemove The user you want to remove
	 */
	public void removeFromMyContacts(User userToRemove) {
		myContacts.remove(userToRemove);
	}
	
	public Map<User, Boolean> getMyContacts() {
		return myContacts;
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

	public Object getPassword() {
		return password;
	}
	
	public void setContactSelection(User user, boolean selected) {
		myContacts.put(user, selected);
	}

	/**
	 * Sets isContactSeleced(User u) to false for every user
	 * in myContacts.
	 */
	public void unselectAllContacts() {
		Iterator<User> userIt = myContacts.keySet().iterator();
		while(userIt.hasNext())
			setContactSelection(userIt.next(), false);
	}
	
	public boolean isContactSelected(User user) {
		return myContacts.get(user);
	}
}
