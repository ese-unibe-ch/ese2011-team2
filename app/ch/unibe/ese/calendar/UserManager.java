package ch.unibe.ese.calendar;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import ch.unibe.ese.calendar.User.DetailedProfileVisibility;
import ch.unibe.ese.calendar.impl.UserManagerImpl;

public abstract class UserManager {

	private static UserManager instance;

	public abstract Set<User> getAllUsers();

	public abstract Map<String, User> getUserByRegex(String regex);

	public abstract User getUserByName(String userName);

	public abstract User createUser(String userName, String password,
			Date birthday, DetailedProfileVisibility detailedProfileVisibility);

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

	public UserManager() {
		super();
	}

}