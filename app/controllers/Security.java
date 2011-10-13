package controllers;
 
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
 
public class Security extends Secure.Security {
	
    static boolean authenticate(String username, String password) {
        UserManager um = UserManager.getInstance();
        User claimedUser = um.getUserByName(username);
        return (claimedUser != null ) && claimedUser.getPassword().equals(password);
    }
    
}