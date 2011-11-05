package controllers;

import java.text.ParseException;
import java.util.Date;

import ch.unibe.ese.calendar.CalendarEvent;
import ch.unibe.ese.calendar.CalendarManager;
import ch.unibe.ese.calendar.EseCalendar;
import ch.unibe.ese.calendar.User;
import ch.unibe.ese.calendar.UserManager;
import ch.unibe.ese.calendar.Visibility;
import ch.unibe.ese.calendar.EventSeries.Repetition;
import ch.unibe.ese.calendar.exceptions.EventNotFoundException;
import ch.unibe.ese.calendar.util.EseDateFormat;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class ModifyEvent extends Controller {
	public static void createEvent(String calendarName, String name, String startDate, 
			String duration, String visibility, String repetition, String description)
			throws Throwable {
		Date sDate = EseDateFormat.getInstance().parse(startDate);
		int minDur = Integer.parseInt(duration);
		Date eDate = new Date();
		eDate.setTime(sDate.getTime()+1000*60*minDur);
		Visibility vis = Visibility.valueOf(visibility.toUpperCase());
		//Date eDate = EseDateFormat.getInstance().parse(endDate); //old version
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		final EseCalendar calendar = CalendarManager.getInstance().getCalendar(
				calendarName);
		if (repetition.equalsIgnoreCase("never")) {
			calendar.addEvent(user, sDate, eDate, name, vis, description);
		} else {
			calendar.addEventSeries(user, sDate, eDate, name, vis, 
					Repetition.valueOf(repetition.toUpperCase()), description);
		}
		Application.calendar(calendarName);
	}
	
	/**
	 * An event is identified by its unique id. For finding it
	 * easily, we have to know it's startDate.
	 * 
	 * @param calendarName
	 * @param hash hashCode() of the to be deleted event
	 */
	public static void deleteEvent(String calendarName, String id,
			String startDate, boolean isSeries) throws ParseException {
		final EseCalendar calendar = CalendarManager.getInstance().getCalendar(
				calendarName);
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		CalendarEvent event = calendar.getEventById(user, id);
		if (isSeries) {
			render(user, calendar, event);
		}
		calendar.removeEvent(user, id);
		Application.calendar(calendarName);
	}
	
	public static void deleteWholeSeries(String calendarName, String id) {
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		final EseCalendar calendar = CalendarManager.getInstance().getCalendar(calendarName);
		calendar.removeEventSeries(user, id);
		Application.calendar(calendarName);
	}
	
	/**
	 * 
	 * @param calendarName
	 * @param id The id of a SerialEvent
	 */
	public static void deleteSingleSerialEvent(String calendarName, String id) {
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		final EseCalendar calendar = CalendarManager.getInstance().getCalendar(calendarName);
		CalendarEvent event = calendar.getEventById(user, id);
		event.getSeries().addExceptionalInstance(id, null);
		Application.calendar(calendarName);
	}

	public static void editEvent(String calendarName, String id, 
			String startDate, boolean isSeries, String repetition)
			throws ParseException {
		final EseCalendar calendar = CalendarManager.getInstance().getCalendar(
				calendarName);
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		Visibility[] visibilities = Visibility.values();
		try {
			CalendarEvent event = calendar.getEventById(user, id);
			//this is quite ugly:
			boolean[] repChecked = new boolean[4];
			if (event.getSeries() == null) {
				repChecked[0]  = true;
			} else {
				repChecked[event.getSeries().getRepetition().ordinal() + 1] = true;
			}
			render(calendar, event, visibilities, repChecked);
		} catch (EventNotFoundException exception) {
			error(exception.getMessage());
		}
	}
	
	public static void saveEditedEvent(String calendarName, String id, String oldStartDate, 
			String name, String startDate, String duration, String visibility, 
			String description, String repetition, boolean wasSeries) 
			throws ParseException {
		
		Date sDate = EseDateFormat.getInstance().parse(startDate);
		int minDur = Integer.parseInt(duration);
		Date eDate = new Date();
		eDate.setTime(sDate.getTime()+1000*60*minDur);
		Visibility vis = Visibility.valueOf(visibility.toUpperCase());
		//Date eDate = EseDateFormat.getInstance().parse(endDate);
		final EseCalendar calendar = CalendarManager.getInstance().getCalendar(
				calendarName);
		String userName = Security.connected();
		User user = UserManager.getInstance().getUserByName(userName);
		
		if (wasSeries) {
			calendar.removeEventSeries(user, id);
		} else {
			calendar.removeEvent(user, id);
		}
		if (repetition.equalsIgnoreCase("never")) {
			calendar.addEvent(user, sDate, eDate, name, vis, 
					description);
		} else {
			calendar.addEventSeries(user, sDate, eDate, name, vis, 
					Repetition.valueOf(repetition.toUpperCase()), description);
		}
		Application.calendar(calendarName);
	}
}