package ch.unibe.ese.calendar.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.User.DetailedProfileVisibility;
import ch.unibe.ese.calendar.exceptions.UserAlreadyExistsException;
import ch.unibe.ese.calendar.impl.UserManagerImpl;
import ch.unibe.ese.calendar.security.PermissionDeniedException;



public class UserManagerTest extends UnitTest {

	private UserManager um;
	private User gamma;
	private User gamm;
	
	@Before
	public void setUp() {
		um = UserManagerImpl.getInstance();
		um.purge(User.ADMIN);
		gamma = um.createUser("gamma", "delta", null, DetailedProfileVisibility.PRIVATE);
		gamm = um.createUser("gamm", "delt", null, DetailedProfileVisibility.PRIVATE);
	}
	
	@Test
	public void createAndRetrieveUser() {
		
		User createdUser = um.createUser("beta", "tester", null, DetailedProfileVisibility.PRIVATE);
		assertNotNull(createdUser);
		User retrievedUser = um.getUserByName("beta");
		assertEquals(createdUser, retrievedUser);
		assertEquals("tester", retrievedUser.getPassword());
		User user2 = um.createUser("beta2", "tester", null, DetailedProfileVisibility.PRIVATE);
		assertTrue(um.getAllUsers().contains(user2));
	}
	
	@Test
	public void testRegex() {
		Set<User> foundUsers = um.getUserByRegex("ga.*");
		assertTrue(foundUsers.contains(gamma));
		assertTrue(foundUsers.contains(gamm));
	}
	
	@Test
	public void testRegex2() {
		Set<User> foundUsers = um.getUserByRegex("gamma.*");
		assertTrue(foundUsers.contains(gamma));
		assertFalse(foundUsers.contains(gamm));
	}
	
	@Test
	public void testRegex3() {
		Set<User> foundUsers = um.getUserByRegex("ni.*");
		assertFalse(foundUsers.contains(gamma));
		assertFalse(foundUsers.contains(gamm));
	}
	
	@Test
	public void testGetAllUsers() {
		Set<User> users = um.getAllUsers();
		assertTrue(users.contains(gamma));
	}
	
	@Test
	public void testDeleteUser() {
		um.deleteUser("gamma");
		Set<User> users = um.getAllUsers();
		assertFalse(users.contains(gamma));
		assertTrue(users.contains(gamm));
	}
	
	@Test(expected=UserAlreadyExistsException.class)
	public void userNameShouldBeUnique() {
		um.createUser("gamma", "whatev", null, DetailedProfileVisibility.PRIVATE);
	}
	
	@Test(expected=PermissionDeniedException.class)
	public void normalUserShouldNotPurgeUserManager() {
		um.purge(gamma);
	}
}
