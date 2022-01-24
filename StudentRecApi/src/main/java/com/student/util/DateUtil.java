package com.student.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtil {
	public static final List<SimpleDateFormat> datePatterns = Arrays.asList(new SimpleDateFormat("d/MM/yyyy H:mm"),
			new SimpleDateFormat("d/MM/yyyy"), new SimpleDateFormat("dd/MM/yyyy"));

	public static Date stringToDate(String inputStringDate) {
		for (SimpleDateFormat pattern : datePatterns) {
			try {
				// Take a try
				return new Date(pattern.parse(inputStringDate).getTime());

			} catch (ParseException pe) {
				// Loop on
			}
		}
		return null;
	}

	public static boolean validateDateFormat(String dateStr) {

		for (SimpleDateFormat pattern : datePatterns) {
			try {
				// Take a try
				Date date = new Date(pattern.parse(dateStr).getTime());
				return true;
			} catch (ParseException pe) {
				// Loop on
			}
		}
		return false;
	}
	
	public static int getDiffYears(Date first, Date last) {
	    Calendar a = getCalendar(first);
	    Calendar b = getCalendar(last);
	    int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
	    if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || 
	        (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
	        diff--;
	    }
	    return diff;	
	}

	public static Calendar getCalendar(Date date) {
	    Calendar cal = Calendar.getInstance(Locale.US);
	    cal.setTime(date);
	    return cal;
	}

}
