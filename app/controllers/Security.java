package controllers;
 
import java.util.Date;

import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.User.DetailedProfileVisibility;
import ch.unibe.ese.calendar.impl.UserManagerImpl;
import ch.unibe.ese.calendar.util.EseDateFormat;
 
public class Security extends Secure.Security {
	
    static boolean authenticate(String username, String password) {
        UserManager um = UserManagerImpl.getInstance();
        User claimedUser = um.getUserByName(username);
        return (claimedUser != null ) && claimedUser.getPassword().equals(password);
    }
    
    public static void addUser(String username, String password, String birthday) throws Throwable{
    	System.out.println(username + "-" + password);
    	
    	UserManager um = UserManagerImpl.getInstance();
    	Date newUsersBirthday = EseDateFormat.getInstance().parse(birthday);
    	um.createUser(username, password, newUsersBirthday, DetailedProfileVisibility.PUBLIC);
    	Secure.login();
    }
}