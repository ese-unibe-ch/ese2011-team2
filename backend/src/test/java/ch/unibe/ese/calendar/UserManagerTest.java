package ch.unibe.ese.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;



public class UserManagerTest {

	@Test
	public void createAndRetrieveUser() {
		UserManager um = UserManager.getInstance();
		User createdUser = um.createUser("beta", "tester");
		assertNotNull(createdUser);
		User retrievedUser = um.getUserByName("beta");
		assertEquals(createdUser, retrievedUser);
		assertEquals("tester", retrievedUser.getPassword());
		um.createUser("beta2", "tester");
		assertEquals(2, um.getAllUsers().size());
	}
}
