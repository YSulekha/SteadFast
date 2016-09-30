package com.nanodegree.alse.todo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * ContentProvider class for Todo DB
 */
public class TaskProvider extends ContentProvider {

    private static final int TASK = 100;
    private static final int TASK_WITH_DATE = 101;
    private static final int TASK_WITH_NAME_AND_DATE = 102;

    private static final String sDateSelection = TaskContract.TaskEntry.TABLE_NAME+"."+TaskContract.TaskEntry.COLUMN_TASK_DATE+" = ?";
    private static final String sDateAndTaskNameSelection = TaskContract.TaskEntry.TABLE_NAME+"."+
            TaskContract.TaskEntry.COLUMN_TASK_DATE+" = ?"+
            " AND "+TaskContract.TaskEntry.TABLE_NAME+"."+
            TaskContract.TaskEntry.COLUMN_TASK_NAME+"= ?";


    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public TaskdbHelper mDbHelper;


    static UriMatcher buildUriMatcher(){
        UriMatcher urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = TaskContract.CONTENT_AUTHORITY;

        urimatcher.addURI(authority,TaskContract.PATH_TASK,TASK);
        urimatcher.addURI(authority,TaskContract.PATH_TASK+"/#",TASK_WITH_DATE);
        urimatcher.addURI(authority,TaskContract.PATH_TASK+"/*/#",TASK_WITH_NAME_AND_DATE);


        return urimatcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new TaskdbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String s, String[] strings1, String sort) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Log.v("ContentProvider_query",String.valueOf(match));
        Cursor retCursor;
        switch (match){
            case TASK:
                retCursor= db.query(TaskContract.TaskEntry.TABLE_NAME,projection,s,strings1,null,null,sort);
                break;
            case TASK_WITH_DATE:
                long date = TaskContract.TaskEntry.getDateFromUri(uri);
                String[] selectionArgs = new String[]{Long.toString(date)};
                retCursor= db.query(TaskContract.TaskEntry.TABLE_NAME,projection,sDateSelection,selectionArgs,null,null,sort);
                break;
            case TASK_WITH_NAME_AND_DATE:
                long dateValue = TaskContract.TaskEntry.getDateFromUriWithTaskName(uri);
                String name = TaskContract.TaskEntry.getTaskNameFromUri(uri);
                String[] args = new String[]{Long.toString(dateValue),name};
                retCursor= db.query(TaskContract.TaskEntry.TABLE_NAME,projection,sDateAndTaskNameSelection,args,null,null,sort);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri"+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        String type;
        int match = sUriMatcher.match(uri);
        switch (match){
            case TASK:
                return TaskContract.TaskEntry.CONTENT_DIR_TYPE;
            case TASK_WITH_DATE:
                return TaskContract.TaskEntry.CONTENT_DIR_TYPE;
            case TASK_WITH_NAME_AND_DATE:
                return TaskContract.TaskEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri"+uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Log.v("Inside insert","ContentProvider");
        int match = sUriMatcher.match(uri);
        Log.v("Inside insert",String.valueOf(match));
        Uri retUri;
        switch (match){
            case TASK:
                long id = db.insert(TaskContract.TaskEntry.TABLE_NAME,null,contentValues);

                if(id > 0){
                    retUri= TaskContract.TaskEntry.buildTaskUri(id);
                }
                else{
                    throw new SQLException("Error inserting record");
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri"+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        int retValue;
        switch (match) {
            case TASK:
                retValue = db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        if(retValue!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return retValue;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int retValue;
        switch (match) {
            case TASK:
                retValue = db.update(TaskContract.TaskEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retValue;
    }
}
