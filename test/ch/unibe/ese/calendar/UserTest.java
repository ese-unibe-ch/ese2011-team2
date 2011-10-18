package ch.unibe.ese.calendar;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UserTest{
	
	User uOne, uTwo, uNull;
	
	@Before
	public void setUp(){
		uOne = new User("userOne", "password1");
		uTwo = new User("userTwo", "password2");
		uNull = new User(null, null);
	}
	
	@Test
	public void userGetName(){
		assertEquals(uOne.getName(), "userOne");
		assertEquals(uTwo.getName(), "userTwo");
		assertNull(uNull.getName());
	}
	
	@Test
	public void userGetPassword(){
		assertEquals(uOne.getPassword(), "password1");
		assertEquals(uTwo.getPassword(), "password2");
		assertNull(uNull.getPassword());
	}
	
	@Test
	public void createUserWithoutPW(){
		User uNoPW = new User("userPWLess");
		assertEquals(uNoPW.getName(), "userPWLess");
		assertNotNull(uNoPW.getPassword());	
	}
	
	@Test
	public void userEqual(){
		//Reflexivity
		assertTrue(uOne.equals(uOne));
		assertTrue(uTwo.equals(uTwo));
		//Symmetry
		User uOneClone = uOne;
		assertEquals(uOne.equals(uOneClone), uOneClone.equals(uOne));
		assertEquals(uTwo.equals(uOneClone), uOneClone.equals(uTwo));
		//Transitivity
		User uOneCloneClone = uOneClone;
		assertTrue(uOne.equals(uOneClone));
		assertTrue(uOneClone.equals(uOneCloneClone));
		assertTrue(uOne.equals(uOneCloneClone));
		//Null Comparison and Consistency
		assertFalse(uOne.equals(null));
		assertFalse(uOne.equals(null));
		
		//Hash Comparison
		assertEquals(uOne.hashCode(), uOneClone.hashCode());
		
		//else
		assertFalse(uNull.equals(null));
		assertFalse(uOne.equals("bla"));
		assertFalse(uNull.equals(uOne));
		User uOneSameName = new User("userOne", "paswrd");
		assertTrue(uOne.equals(uOneSameName));
	}
	
	@Test
	public void generatingHashCode(){
		assertEquals(uOne.hashCode(), uOne.hashCode());
		assertEquals(uOne.hashCode() - uTwo.hashCode(),
				-(uTwo.hashCode() - uOne.hashCode()));
		assertEquals(uNull.hashCode(), 31);
	}
	
	@Test
	public void userToString(){
		assertEquals(uOne.toString(), "User [userName=userOne]");
		assertEquals(uTwo.toString(), "User [userName=userTwo]");
		assertEquals(uNull.toString(), "User [userName=null]");
	}
	
}
