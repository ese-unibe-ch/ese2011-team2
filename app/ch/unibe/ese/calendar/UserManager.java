package ch.unibe.ese.calendar;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import ch.unibe.ese.calendar.User.DetailedProfileVisibility;

/**
 * Manages the users in a system
 *
 */
public class UserManager {

	private static UserManager instance;
	private Map<String, User> users = new HashMap<String, User>();
	
	private UserManager() {
		
	}
	
	/**
	 * 
	 * @return the singleton instance
	 */
	public static UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}
		return instance;
	}
	/**
	 * Creates a new user with the specified properties
	 * 
	 */
	public synchronized User createUser(String userName, String password, 
			Date birthday, DetailedProfileVisibility detailedProfileVisibility) {
		User user = new User(userName, password, birthday, detailedProfileVisibility);
		users.put(userName, user);
		return user;
	}
	
	@Deprecated
	public synchronized User createUser(String userName, String password, 
			Date birthday, String detailedProfileVisibility) {
		User user = new User(userName, password, birthday, detailedProfileVisibility);
		users.put(userName, user);
		return user;
	}

	/**
	 * 
	 * @param userName
	 * @return the user with that name or null if no such user exists
	 */
	public synchronized User getUserByName(String userName) {
		return users.get(userName);
	}
	
	/**
	 * Get users with names matching a specific regex
	 * 
	 * @param regex
	 * @return
	 */
	//TODO should just return Set<User>
	public synchronized Map<String, User> getUserByRegex(String regex) {		
		Map<String, User> foundUsers = new HashMap<String, User>();
		for (User u: users.values()) {
			if (Pattern.matches(regex, u.getName()))
				foundUsers.put(u.getName(), u);
		}
		return foundUsers;
	}
	
	/**
	 * @return an unmodifiable set of all users
	 */
	public synchronized Set<User> getAllUsers() {
		Set<User> result = new HashSet<User>();
		result.addAll(users.values());
		return Collections.unmodifiableSet(result);
	}
	
	
}
