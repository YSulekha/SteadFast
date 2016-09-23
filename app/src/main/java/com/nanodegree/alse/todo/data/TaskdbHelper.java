package com.nanodegree.alse.todo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class to create database
 */
public class TaskdbHelper extends SQLiteOpenHelper {

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    static final String DATABASE_NAME = "todo.db";

    public TaskdbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Craete table SQL statement
         final String SQL_CREATE_TABLE = "CREATE TABLE "+TaskContract.TaskEntry.TABLE_NAME+" ("+

                TaskContract.TaskEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TaskContract.TaskEntry.COLUMN_TASK_NAME+" TEXT NOT NULL,"+
                TaskContract.TaskEntry.COLUMN_TASK_DATE+" INTEGER NOT NULL,"+
                TaskContract.TaskEntry.COLUMN_TASK_PRIORITY+" TEXT NOT NULL,"+
                TaskContract.TaskEntry.COLUMN_TASK_STATUS+" TEXT NOT NULL );";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //OnUpgrade drop the table and create new table
        //----TEMPORARY CHANGE ONCE YOU HAVE THE UPDATE
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
