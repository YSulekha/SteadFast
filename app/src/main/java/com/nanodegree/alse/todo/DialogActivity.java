package com.nanodegree.alse.todo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nanodegree.alse.todo.data.TaskContract;
import com.nanodegree.alse.todo.data.Utility;

import java.util.Calendar;

/**
 * Created by aharyadi on 9/23/16.
 */
public class DialogActivity extends AppCompatActivity {

    EditText mName;
    public static EditText mDate;
    Spinner mPriority;
    Spinner mStatus;
    EditText mNotes;
    public static String dateValue;
    static long dateInMilliseconds;
    static public String isEdit;
    public static final String ISEDIT = "is_edit";
    ArrayAdapter<CharSequence> priority_adapter;
    ArrayAdapter<CharSequence> status_adapter;
    long id;

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            dateValue = Utility.setdateValue(year, month, day);

            String dateString = day+"-"+(month+1)+"-"+year;
            String dayName = Utility.getDayName(dateValue);
            mDate.setText(dayName);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year;
            int month;
            int day;

            if(isEdit.equals("true")){
                String v = dateValue;
                year = Integer.parseInt(v.substring(0, 4));
                month = Integer.parseInt(v.substring(4,6))-1;
                day = Integer.parseInt(v.substring(6));
            }
            else {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                dateValue = Utility.setdateValue(year, month, day);
                Log.v("DateValueInsert",dateValue);
            }
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this,year,month,day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            return datePickerDialog;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        getWindow().setGravity(Gravity.BOTTOM);
        isEdit = getIntent().getStringExtra(ISEDIT);

        Log.v("InsideOnCreate", String.valueOf(isEdit));

        mName = (EditText)findViewById(R.id.dialog_task_name);
         mDate = (EditText)findViewById(R.id.dialog_task_date);
        mDate.setText("Today");
        dateValue = Utility.setTodayDate();
         mPriority = (Spinner)findViewById(R.id.dialog_task_priority);
        mStatus = (Spinner)findViewById(R.id.dialog_task_status);
        mNotes = (EditText)findViewById(R.id.dialog_task_notes);

        priority_adapter = ArrayAdapter.createFromResource(this, R.array.priority_levels, R.layout.spinner_text);
        priority_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPriority.setAdapter(priority_adapter);


        status_adapter = ArrayAdapter.createFromResource(this, R.array.status_levels,  R.layout.spinner_text);
        status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStatus.setAdapter(status_adapter);

        if (getIntent() != null && getIntent().getData() != null) {
            Uri uri = getIntent().getData();
            Log.v("Dialog",uri.toString());
            Cursor cur = getContentResolver().query(uri,null,null,null,null);
            if(cur.moveToFirst()) {
                String name = cur.getString(cur.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME));
                long curDate = cur.getLong(cur.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DATE));
                id = cur.getLong(cur.getColumnIndex(TaskContract.TaskEntry._ID));
                dateValue = Long.toString(curDate);
                String prior = cur.getString(cur.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_PRIORITY));
                String status = cur.getString(cur.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_STATUS));
                String notes = cur.getString(cur.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NOTES));
                Log.v("InsideOnCreateDi", name);
                mName.setText(name);
                mDate.setText(Utility.getDayName(Long.toString(curDate)));
                mPriority.setSelection(priority_adapter.getPosition(prior));
                mStatus.setSelection(status_adapter.getPosition(status));
                mNotes.setText(notes);
                //mPriority.setText(prior);
               // mStatus.setText(status);
            }
        }
    }

    public void insertNewRecord(View view){
        ContentValues values = new ContentValues();
        if(mName.getText().toString().trim().equals("")) {
            mName.setError("Name is a required field");
        }
        else {
            String name = mName.getText().toString();

            String date = mDate.getText().toString();
            String note = mNotes.getText().toString();

            Log.v("DateValueInsert", dateValue);

            String priority = mPriority.getSelectedItem().toString();
            String status = mStatus.getSelectedItem().toString();
            values.put(TaskContract.TaskEntry.COLUMN_TASK_NAME, name);
            values.put(TaskContract.TaskEntry.COLUMN_TASK_DATE, dateValue);
            values.put(TaskContract.TaskEntry.COLUMN_TASK_PRIORITY, priority);
            values.put(TaskContract.TaskEntry.COLUMN_TASK_STATUS, status);
            values.put(TaskContract.TaskEntry.COLUMN_TASK_NOTES, note);
            if (isEdit.equals("true")) {
                //If editing the record update the database
                Log.v("InsideInsert","IsEdit");
                updateRecord(values);
            } else {
                //else insert new record
                getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, values);
            }
            finish();
        }

    }

    public void deleteRecord(View view){

        String where =   TaskContract.TaskEntry.TABLE_NAME+"."+
                TaskContract.TaskEntry._ID+" = ?";
        String[] args = new String[]{Long.toString(id)};
        getContentResolver().delete(TaskContract.TaskEntry.CONTENT_URI, where, args);
        Toast toast = Toast.makeText(view.getContext(),"Record deleted",Toast.LENGTH_LONG);
        toast.show();
        finish();
    }

    private void updateRecord(ContentValues values){

        String where =   TaskContract.TaskEntry.TABLE_NAME+"."+
                TaskContract.TaskEntry._ID+" = ?";
        String[] args = new String[]{Long.toString(id)};
        getContentResolver().update(TaskContract.TaskEntry.CONTENT_URI,values,where,args);

    }
    public void getDate(View view){
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(),"DateSelection");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
