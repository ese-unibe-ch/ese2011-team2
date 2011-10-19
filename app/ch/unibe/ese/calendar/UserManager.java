package ch.unibe.ese.calendar;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class UserManager {

	private static UserManager instance;
	private Map<String, User> users = new HashMap<String, User>();
	
	private UserManager() {
		
	}

	public static UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}
		return instance;
	}
	
	public synchronized User createUser(String userName, String password) {
		User user = new User(userName, password);
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
