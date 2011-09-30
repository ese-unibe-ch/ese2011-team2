package jobs;
import ch.unibe.ese.calendar.UserManager;
import play.jobs.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
    
    public void doJob() {
        UserManager um = UserManager.getIsntance();
        um.createUser("aaron", "ese");
        //TODO add a default calendar
    }
    
}