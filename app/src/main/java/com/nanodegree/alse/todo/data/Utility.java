package com.nanodegree.alse.todo.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utility {

    public static final String DATE_FORMAT = "yyyyMMdd";

    //method to get the Day name of the date
    public static String getDayName(String input) {
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = inFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE, MMM dd");
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String dateValue = setdateValue(year, month, day);
        if (input.equals(dateValue)) {
            return "Today";
        }
        c.add(Calendar.DAY_OF_MONTH, 1);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        if (input.equals(setdateValue(year, month, day))) {
            return "Tomorrow";
        }
        return shortenedDateFormat.format(date);

    }

    //Formatting the values to long "yyyymmdd"
    public static String setdateValue(int year, int month, int day) {
        String value = String.valueOf(year);
        if ((month + 1) < 10) {
            value = value + "0" + String.valueOf(month + 1);
        } else {
            value = value + String.valueOf(month + 1);
        }
        if (day < 10) {
            value = value + "0" + String.valueOf(day);
        } else {
            value = value + String.valueOf(day);
        }
        return value;
    }

    public static String setTodayDate() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String dateValue = setdateValue(year, month, day);
        return dateValue;
    }

}
