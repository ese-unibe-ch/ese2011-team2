package ch.unibe.ese.calendar;

import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class DateFormatTest extends UnitTest{
	
	@Test
	public void parseShouldSucceed() throws ParseException {
		Date testDate = EseDateFormat.getInstance().parse("20.05.2011 13:20");
		assertEquals("20.05.2011 13:20", EseDateFormat.getInstance().format(testDate));
	}
	
	@Test(expected=ParseException.class) 
	public void wrongStringFormat() throws ParseException {
		Date testDate = EseDateFormat.getInstance().parse("2001.05.01 13:20");
	}
	
	@Test(expected=ParseException.class) 
	public void rubbishString() throws ParseException {
		Date testDate = EseDateFormat.getInstance().parse("0MGLOL");
	}
}
