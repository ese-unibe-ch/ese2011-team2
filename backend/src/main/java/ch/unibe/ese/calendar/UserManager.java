package ch.unibe.ese.calendar;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

	private static UserManager instance;
	private Map<String, User> users = new HashMap<String, User>();
	
	private UserManager() {
		
	}

	public static UserManager getIsntance() {
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
	
	
}
