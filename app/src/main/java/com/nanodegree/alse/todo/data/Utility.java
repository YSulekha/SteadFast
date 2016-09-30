package com.nanodegree.alse.todo.data;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

import com.nanodegree.alse.todo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by aharyadi on 9/23/16.
 */
public class Utility {

    public static final String DATE_FORMAT = "yyyyMMdd";

    public static String getFriendlyDayString(Context context, int date,int month,int year) {
        // The day string for forecast uses the following logic:
        // For today: "Today, June 8"
        // For tomorrow:  "Tomorrow"
        // For the next 5 days: "Wednesday" (just the day name)
        // For all days after that: "Mon Jun 8"


        long dateInMillis = 0;
        try {
            dateInMillis = getDateinMilliseconds(date, month, year);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Time time = new Time();
        time.setToNow();
        long currentTime = System.currentTimeMillis();
        int julianDay = Time.getJulianDay(dateInMillis, time.gmtoff);
        int currentJulianDay = Time.getJulianDay(currentTime, time.gmtoff);

        // If the date we're building the String for is today's date, the format
        // is "Today, June 24"
        if (julianDay == currentJulianDay) {
            String today = context.getString(R.string.today);
            int formatId = R.string.format_full_friendly_date;
            return String.format(context.getString(
                    formatId,
                    today,
                    getFormattedMonthDay(context, dateInMillis)));
        } else if ( julianDay < currentJulianDay + 7 ) {
            // If the input date is less than a week in the future, just return the day name.
            return getDayName(context, dateInMillis);
        } else {
            // Otherwise, use the form "Mon Jun 3"
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(dateInMillis);
        }
    }
    public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if ( julianDay == currentJulianDay + 1) {
            return context.getString(R.string.tomorrow);
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }

    /**
     * Converts db date format to the format "Month day", e.g "June 24".
     * @param context Context to use for resource localization
     * @param dateInMillis The db formatted date string, expected to be of the form specified
     *                in Utility.DATE_FORMAT
     * @return The day in the form of a string formatted "December 6"
     */
    public static String getFormattedMonthDay(Context context, long dateInMillis ) {
        Time time = new Time();
        time.setToNow();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(Utility.DATE_FORMAT);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM dd");

        String monthDayString = monthDayFormat.format(dateInMillis);
        Log.v("getFormattedMonthDay", monthDayString);
        return monthDayString;
    }

    public static long getDateinMilliseconds(int date,int month,int year) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = Integer.toString(year);
        if(month < 10){
            d = d + "0"+month+date;
        }
        else
            d = d+month+date;
        Date date1 = format.parse(d);
        return date1.getTime();

    }
    public static String getDayName(String input){
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = inFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.v("DateUtility",date.toString());
   //     SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE, MMM dd");
        Calendar c = Calendar.getInstance();
        int year =c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String dateValue = setdateValue(year,month,day);
   /*     if((month+1)<10){
            dateValue = dateValue+"0"+ String.valueOf(month+1);
        }
        else{
            dateValue = dateValue+ String.valueOf(month+1);
        }
        if(day <10){
            dateValue = dateValue+"0"+String.valueOf(day);
        }
        else{
            dateValue = dateValue+String.valueOf(day);
        }*/
        Log.v("Stored+Today", input + "  " + dateValue);
        if(input.equals(dateValue)){
            return "Today";
        }
        c.add(Calendar.DAY_OF_MONTH,1);
        year =c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        if(input.equals(setdateValue(year,month,day))){
            return "Tomorrow";
        }
        Log.v("DateUtility+day",shortenedDateFormat.format(date));
        return shortenedDateFormat.format(date);
       // String goal = outFormat.format(date);
        //return goal;
    }
    public static String setdateValue(int year, int month, int day){
        String value = String.valueOf(year);
        if((month+1)<10){
            value = value+"0"+ String.valueOf(month+1);
        }
        else{
            value = value+ String.valueOf(month+1);
        }
        if(day <10){
            value = value+"0"+String.valueOf(day);
        }
        else{
            value = value+String.valueOf(day);
        }
        return value;
    }

    public static String setTodayDate(){
        Calendar c = Calendar.getInstance();
        int year =c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String dateValue = setdateValue(year,month,day);
        return dateValue;
    }

}
