package controllers;
 
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import models.*;
 
public class Security extends Secure.Security {
	
    static boolean authenticate(String username, String password) {
        UserManager um = UserManager.getIsntance();
        User claimedUser = um.getUserByName(username);
        return (claimedUser != null ) && claimedUser.getPassword().equals(password);
    }
    
}