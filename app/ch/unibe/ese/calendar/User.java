package ch.unibe.ese.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.activity.InvalidActivityException;

import ch.unibe.ese.calendar.util.UserComparator;



/**
 * Represents a User. Users have a unique names identifying them and
 * a Map myContacts, containing the contacts as keys and a Boolean as value
 * indicating if the contact is selected or not.
 *
 */
public class User {
	
	public static final User ADMIN = new User("admin");

	private String userName;
	private Object password;
	private Map<User, Boolean> myContacts = new HashMap<User, Boolean>();
	
	public static enum DetailedProfileVisibility {
		PRIVATE, PUBLIC, CONTACTSONLY
	}
	
	private DetailedProfileVisibility detailedProfileVisibility;
	
	public User(String userName, String password, DetailedProfileVisibility detailedProfileVisibility) {
		this.userName = userName;
		this.password = password;
		this.detailedProfileVisibility = detailedProfileVisibility;
		myContacts = new HashMap<User, Boolean>();
	}

	/**
	 * creates a user with the specified username and a random password
	 * @param userName
	 */
	public User(String userName) {
		this(userName, Integer.toString((int)(Math.random()*1000)), DetailedProfileVisibility.PRIVATE);
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
	 * Removes a user from the Map myContacts.
	 * There is no return value.
	 * @param userToRemove The user you want to remove
	 * @throws InvalidActivityException if user tries to remove himself
	 * from myContacts.
	 */
	public void removeFromMyContacts(User userToRemove) 
			throws InvalidActivityException {
		if (userToRemove.equals(this)) {
			throw new InvalidActivityException("You can't remove " +
					"yourself from your contacts");
		}
		myContacts.remove(userToRemove);
	}
	
	/**
	 * Gets all users added to contacts as a Map.
	 * 
	 * @return a Map containing all myContacts as a key with a 
	 * boolean value, which declares if the contact is selected or not.
	 */
	public Map<User, Boolean> getMyContacts() {
		return myContacts;
	}
	
	/**
	 * Gets all user in myContacts in a SortedSet.
	 * 
	 * @return a SortedSet containing all contacts, always starting with 
	 * the connected User. The other users are sorted alphabetically.
	 */
	public SortedSet<User> getSortedContacts() {
		SortedSet sortedContacts = new TreeSet<User>(new UserComparator(this));
		sortedContacts.addAll(myContacts.keySet());
		return sortedContacts;
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

	/**
	 * Gets the name of the user.
	 * @return the name of the user as a String.
	 */
	public String getName() {
		return userName;
	}

	/**
	 * Gets the password of the user.
	 * @return the password of the user as an Object.
	 */
	public Object getPassword() {
		return password;
	}
	
	/**
	 * Gets the detailed profile visibility of a user
	 * @return the detailed profile visibility
	 */
	public DetailedProfileVisibility getDetailedProfileVisibility() {
		return detailedProfileVisibility;
	}
	
	/**
	 * Sets the selection value of a specified user to
	 * passed boolean.
	 * @param user
	 * , whose selection value is to be changed.
	 * @param selected
	 * , a boolean used to select a user if true,
	 * deselect if false.
	 */
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
	
	/**
	 * Gets the selection value as a boolean of a 
	 * specified user.
	 * @param user
	 * , whose selection value is to be returned.
	 * @return a boolean which is true if user
	 * was selected, false if not.
	 */
	public boolean isContactSelected(User user) {
		return myContacts.get(user);
	}
	
}
