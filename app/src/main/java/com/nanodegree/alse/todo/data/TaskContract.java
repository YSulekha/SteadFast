package com.nanodegree.alse.todo.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Table for Todo
 */
public class TaskContract {

    //Authority for contentprovider which is the package name
    public static final String CONTENT_AUTHORITY = "com.nanodegree.alse.todo";

    //Base content uri which contains the content Authority
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //path to be added to Uri of content provider
    public static final String PATH_TASK = "task";

    public static final class TaskEntry implements BaseColumns{


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASK).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;

        public static final String TABLE_NAME = "task";
        //Column for name of the task
        public static final String COLUMN_TASK_NAME = "task_name";
        //priority level of the task
        public static final String COLUMN_TASK_PRIORITY = "priority";
        //Status of the task
        public static final String COLUMN_TASK_STATUS = "status";
        //Todo date
        public static final String COLUMN_TASK_DATE = "date";

        public static long normalizeDate(long startDate) {
            // normalize the start date to the beginning of the (UTC) day
            Time time = new Time();
            time.set(startDate);
            int julianDay = Time.getJulianDay(startDate, time.gmtoff);
            return time.setJulianDay(julianDay);
        }

        //returns uri with appended id
        public static Uri buildTaskUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildUriWithNameAndDate(long date, String name){
            return CONTENT_URI.buildUpon().appendPath(name).appendPath(Long.toString(normalizeDate(date))).build();
        }

        public static Uri buildUriWithDate(long date){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(normalizeDate(date))).build();
        }

        public static String getTaskNameFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static long getDateFromUriWithTaskName(Uri uri){
            return Long.parseLong(uri.getPathSegments().get(2));
        }

        public static long getDateFromUri(Uri uri){
            return Long.parseLong(uri.getPathSegments().get(1));
        }

    }
}
