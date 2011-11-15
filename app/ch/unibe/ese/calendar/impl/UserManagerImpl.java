package ch.unibe.ese.calendar.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.User.DetailedProfileVisibility;
import ch.unibe.ese.calendar.exceptions.UserAlreadyExistsException;
import ch.unibe.ese.calendar.security.CalendarAdminPermission;
import ch.unibe.ese.calendar.security.Policy;

public class UserManagerImpl extends UserManager {

	private Map<String, User> users = new HashMap<String, User>();
	
	@Override
	public synchronized User createUser(String userName, String password, 
			Date birthday, DetailedProfileVisibility detailedProfileVisibility) {
		if (users.containsKey(userName))
			throw new UserAlreadyExistsException(userName);
		User user = new User(userName, password, detailedProfileVisibility);
		users.put(userName, user);
		return user;
	}
	
	@Override
	public synchronized User getUserByName(String userName) {
		return users.get(userName);
	}
	
	@Override
	public synchronized Set<User> getUserByRegex(String regex) {		
		Map<String, User> foundUsers = new HashMap<String, User>();
		for (User u: users.values()) {
			if (Pattern.matches(regex, u.getName()))
				foundUsers.put(u.getName(), u);
		}
		return new HashSet(foundUsers.values());
	}
	
	@Override
	public synchronized Set<User> getAllUsers() {
		Set<User> result = new HashSet<User>();
		result.addAll(users.values());
		return Collections.unmodifiableSet(result);
	}

	@Override
	public synchronized void deleteUser(String userName) {
		users.remove(userName);
	}

	@Override
	public void purge(User user) {
		Policy.getInstance().checkPermission(user, new CalendarAdminPermission());
		users.clear();
	}
	
	
}
