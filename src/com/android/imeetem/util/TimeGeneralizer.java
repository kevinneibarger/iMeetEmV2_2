/**
 * 
 */
package com.android.imeetem.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author kevinscomp
 * 
 */
public class TimeGeneralizer implements Serializable {

	private static final long serialVersionUID = 1L;

	public static TimeGeneralizer getInstance() {
		return new TimeGeneralizer();

	}

	public String getGeneralTime(String timeSent) {

		Date currDate = new Date(System.currentTimeMillis());
		Calendar curCalTime = Calendar.getInstance();
		curCalTime.setTime(currDate);
		String dateParsePattern = "yyyy-MM-dd kk:mm:ss";

		SimpleDateFormat sdf = new SimpleDateFormat(dateParsePattern);

		try {

			Date dateSent = sdf.parse(timeSent);

			Calendar sentCal = Calendar.getInstance();

			sentCal.setTime(dateSent);

			int sentDay = sentCal.get(Calendar.DATE);
			int sentMonth = sentCal.get(Calendar.MONTH) + 1; // Zero based
			int sentYear = sentCal.get(Calendar.YEAR);
			int sentHour = sentCal.get(Calendar.HOUR_OF_DAY);
			int sentMin = sentCal.get(Calendar.MINUTE);
			int currDay = curCalTime.get(Calendar.DATE);
			int currMonth = curCalTime.get(Calendar.MONTH) + 1; // Zero based
			int currYear = curCalTime.get(Calendar.YEAR);
			int currHour = curCalTime.get(Calendar.HOUR_OF_DAY);
			int currMin = curCalTime.get(Calendar.MINUTE);

			int hourDiff = currHour - sentHour; // TODO: Check to make sure time
			// is not in future.. should'nt
			// be..

			int minuteDiff = currMin - sentMin;
			int dayDiff = currDay - sentDay;
			int monthDiff = currMonth - sentMonth;

			if (currDay == sentDay && currMonth == sentMonth
					&& currYear == sentYear && currHour == sentHour
					&& currMin == sentMin) {

				return "Sent Just Now";

			} else if (currDay == sentDay && currMonth == sentMonth
					&& currYear == sentYear && currHour == sentHour) {
				return "Sent " + minuteDiff + " minutes ago";

			} else if (currDay == sentDay && currMonth == sentMonth
					&& currYear == sentYear) {

				if (hourDiff >= 0) {

					if (hourDiff == 0) {
						return "Sent " + minuteDiff + " minutes ago";
					} else if (hourDiff > 0 && hourDiff == 1) {
						return "Sent " + hourDiff + " hour ago";
					} else {
						return "Sent " + hourDiff + " hours ago";
					}

				} else {

					return "Sent hours ago"; // We should NEVER see this... it
					// means hour diff is negative
				}

			} else if (currMonth == sentMonth && currYear == sentYear) {

				if (dayDiff >= 0) {

					if (dayDiff >= 1 && dayDiff < 7) {

						if (dayDiff == 1) {
							return "Sent 1 day ago";
						} else {
							return "Sent " + dayDiff + " days Ago";
						}

					} else if (dayDiff >= 7 && dayDiff < 14) {
						return "Sent 1 week ago";
					} else {
						return "Sent a few weeks ago";
					}

				} else {
					return "Sent a few weeks ago";
				}

			} else if (currYear == sentYear) {
				if (monthDiff == 1) {
					return "Sent " + monthDiff + " month ago";
				} else {
					return "Sent " + monthDiff + " months ago";
				}
			} else {
				return "Sent over a year ago";
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}