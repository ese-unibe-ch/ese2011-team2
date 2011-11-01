package ch.unibe.ese.calendar;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;



public class UserManagerTest extends UnitTest {

	private UserManager um;
	private User gamma;
	private User gamm;
	
	@Before
	public void setUp() {
		um = UserManager.getInstance();
		gamma = um.createUser("gamma", "delta", null, false);
		gamm = um.createUser("gamm", "delt", null, false);
	}
	
	@Test
	public void createAndRetrieveUser() {
		
		User createdUser = um.createUser("beta", "tester", null, false);
		assertNotNull(createdUser);
		User retrievedUser = um.getUserByName("beta");
		assertEquals(createdUser, retrievedUser);
		assertEquals("tester", retrievedUser.getPassword());
		User user2 = um.createUser("beta2", "tester", null, false);
		assertTrue(um.getAllUsers().contains(user2));
	}
	
	@Test
	public void testRegex() {
		Map<String, User> foundUsers = um.getUserByRegex("ga.*");
		assertTrue(foundUsers.containsValue(gamma));
		assertTrue(foundUsers.containsValue(gamm));
	}
	
	@Test
	public void testRegex2() {
		Map<String, User> foundUsers = um.getUserByRegex("gamma.*");
		assertTrue(foundUsers.containsValue(gamma));
		assertFalse(foundUsers.containsValue(gamm));
	}
	
	@Test
	public void testRegex3() {
		Map<String, User> foundUsers = um.getUserByRegex("ni.*");
		assertFalse(foundUsers.containsValue(gamma));
		assertFalse(foundUsers.containsValue(gamm));
	}
}
