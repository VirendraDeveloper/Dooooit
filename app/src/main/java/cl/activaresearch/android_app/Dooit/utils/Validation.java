package cl.activaresearch.android_app.Dooit.utils;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 25 Jul,2018
 */
public class Validation {
    private static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static boolean isEmailValid(String email) {
        return email.matches(emailPattern);
    }

    public static boolean isMobileNumberValid(String strContact) {
        if ((strContact.length() > 6) && (strContact.length() < 13)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getMonthDate(String strDate) {
        String dateDOB = "";
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = sdf1.parse(strDate);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
            dateDOB = sdf2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateDOB;
    }

    public static String getMonthDate(Date date) {
        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String strDate = sdf.format(date);
        Log.d("", "");
        return strDate;
    }

    public static String getDateFormat(Date date) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String strDate = sdf.format(date);
        Log.d("", "");
        return strDate;
    }
    public static String getFormattedDate(Context context, String someDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(someDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(date.getTime());

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd-MMM-yyyy";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return "Today "/* + DateFormat.format(timeFormatString, smsTime)*/;
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            return "Yesterday "/* + DateFormat.format(timeFormatString, smsTime)*/;
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
        }
    }
    public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
