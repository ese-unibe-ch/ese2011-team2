package ch.unibe.ese.calendar;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import ch.unibe.ese.calendar.User.DetailedProfileVisibility;
import ch.unibe.ese.calendar.exceptions.UserAlreadyExistsException;
import ch.unibe.ese.calendar.impl.UserManagerImpl;

/**
 * Manages the users in a system
 *
 */
public abstract class UserManager {

	private static UserManager instance;

	/**
	 * @return an unmodifiable set of all users
	 */
	public abstract Set<User> getAllUsers();

	/**
	 * Get users with names matching a specific regex
	 * 
	 * @param regex
	 * @return
	 */
	public abstract Set<User> getUserByRegex(String regex);

	/**
	 * 
	 * @param userName
	 * @return the user with that name or null if no such user exists
	 */
	public abstract User getUserByName(String userName);

	/**
	 * Creates a new user with the specified properties
	 * @throws UserAlreadyExistsException if the given userName is already in the database
	 */
	public abstract User createUser(String userName, String password,
			Date birthday, DetailedProfileVisibility detailedProfileVisibility);

	
	/**
	 * Deletes an User with the specified userName. 
	 * Doesn't do anything if user is not in Database
	 */
	public abstract void deleteUser(String username);
	

	/**
	 * Permanently deletes all users managed my this UserManager
	 * 
	 * @param user the user requesting the operation
	 */
	public abstract void purge(User user);
	
	/**
	 * 
	 * @return the singleton instance
	 */
	public static UserManager getInstance() {
		if (instance == null) {
			instance = new UserManagerImpl();
		}
		return instance;
	}	
}