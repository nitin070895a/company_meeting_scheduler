package com.example.companymeetingscheduler.Helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Contains all the Utility methods
 *
 * Created by Nitin on 01/09/18.
 */
public class TimeAndDateUtils {

    /**
     * The default date format
     */
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    public static final String DEFAULT_TIME_FORMAT = "hh:mm a";

    /**
     * Returns the string of current date in the {@link TimeAndDateUtils#DEFAULT_DATE_FORMAT}
     * @return the current date in format {@link TimeAndDateUtils#DEFAULT_DATE_FORMAT}
     */
    public static String getCurrentDateInDefaultFormat(){
        return getDateInFormat(getCurrentDate(), DEFAULT_DATE_FORMAT);
    }

    /**
     * Returns the specified {@code date} in the specified {@code format}
     *
     * @param date the date to be formatted
     * @param format the format in which the date is to be formatted
     *
     * @return the string of the {@code date} in the {@code format}
     */
    public static String getDateInFormat(Date date, String format){
        return new SimpleDateFormat(format, Locale.ENGLISH).format(date);
    }

    /**
     * Reutrns the current date instance of the device
     * @return the current date
     */
    public static Date getCurrentDate(){
        return Calendar.getInstance().getTime();
    }

    /**
     * Converts string date from one format to other
     * @param strDate the date to be converted
     * @param fromFormat current format of the date
     * @param toFormat the required format of the date
     * @return the converted date in {@code toFormat}
     */
    public static String convertStringDate(String strDate, String fromFormat, String toFormat) {

        DateFormat originalFormat = new SimpleDateFormat(fromFormat, Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat(toFormat, Locale.ENGLISH);

        try {
            Date date = originalFormat.parse(strDate);
            return targetFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Add some days to the given date and returns the date in the {@code format}
     * @param date the date in which the days are to be added
     * @param days the number of days to be added, can be negative to subtract
     *
     * @return the result date with {@code days} added to the {@code date}
     */
    public static Date addDaysToADate(Date date, int days, String format){

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.add(Calendar.DATE, days);

        return new Date(c.getTimeInMillis());
    }


    /**
     * Converts a date in string format into a calendar instance
     * @param date the string date to be converted
     * @param format the format of the {@code } date
     * @return the calender correspoding to the {@code date} specified
     */
    public static Calendar stringDateToCalendar(String date, String format) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);

        try {
            cal.setTime(sdf.parse(date));
            return cal;
        }
        catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
}
