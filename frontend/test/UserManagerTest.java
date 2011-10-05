

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import play.test.UnitTest;

import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;



public class UserManagerTest extends UnitTest{

	@Test
	public void createAndRetrieveUser() {
		UserManager um = UserManager.getIsntance();
		User createdUser = um.createUser("beta", "tester");
		assertNotNull(createdUser);
		User retrievedUser = um.getUserByName("beta");
		assertEquals(createdUser, retrievedUser);
		assertEquals("tester", retrievedUser.getPassword());
		um.createUser("beta2", "tester");
		assertEquals(2, um.getAllUsers().size());
	}
}
