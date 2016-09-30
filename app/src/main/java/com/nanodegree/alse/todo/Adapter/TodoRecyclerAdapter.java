package com.nanodegree.alse.todo.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.alse.todo.R;
import com.nanodegree.alse.todo.data.TaskContract;

/**
 * Created by aharyadi on 9/21/16.
 */
public class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.TodoViewHolder> implements ItemTouchHelpAdapter{

    Cursor cursor;
    Context context;
    private OnItemClickListener listener;
    private boolean dataIsValid;
    private int rowIdColumn;
    private DataSetObserver mDataSetObserver;
    private TextView emptyView;

    @Override
    public void onItemDismiss(int position) {
        Cursor c = getCursor();

        Log.v("OnItemDismiss",String.valueOf(cursor.getCount()));
        Log.v("OnItemDismiss",String.valueOf(position));
        if(cursor.moveToPosition(position)) {
            Log.v("OnItemDismissIf",String.valueOf(position));
            long id = c.getLong(c.getColumnIndex(TaskContract.TaskEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME));
            Log.v("OnItemDismissIf",String.valueOf(name));
            String where = TaskContract.TaskEntry.TABLE_NAME + "." +
                    TaskContract.TaskEntry._ID + " = ?";
            String[] args = new String[]{Long.toString(id)};
            context.getContentResolver().delete(TaskContract.TaskEntry.CONTENT_URI, where, args);
            notifyItemRemoved(position);
            c.requery();
        //    c.notify();
            notifyDataSetChanged();
           // c.requery();
        }
    }
    @Override
    public long getItemId(int position) {
        if (dataIsValid && cursor!= null && cursor.moveToPosition(position)) {
            return cursor.getLong(rowIdColumn);
        }
        return 0;
    }


    public static interface OnItemClickListener{
        public void onClick(Uri uri,TodoViewHolder vh);
    }

    public TodoRecyclerAdapter(Context c, OnItemClickListener l, Cursor cur){

        context=c;
        listener = l;
        cursor=cur;
        dataIsValid = cursor != null;
        rowIdColumn = dataIsValid ? cursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (dataIsValid) {
            cursor.registerDataSetObserver(mDataSetObserver);

        }
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_listitem,viewGroup,false);
        TodoViewHolder viewHolder = new TodoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodoViewHolder todoViewHolder, int i) {
       cursor.moveToPosition(i);
        Log.v("TodoRecyclerAdapter", cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME)));
       todoViewHolder.todo.setText(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME)));
        todoViewHolder.priority.setText(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_PRIORITY)));
        todoViewHolder.status.setText(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_STATUS)));

    }
    public Cursor getCursor(){
        return cursor;
    }

    @Override
    public int getItemCount() {
        if(null==cursor)
            return 0;
        Log.v("TodoRecyclerAdapter",String.valueOf(cursor.getCount()));
        return cursor.getCount();
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            dataIsValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            dataIsValid = false;
            notifyDataSetChanged();
        }
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{

        TextView todo;
        TextView priority;
        TextView status;

        public TodoViewHolder(View itemView) {
            super(itemView);
            todo = (TextView)itemView.findViewById(R.id.rec_todo_name);
            priority = (TextView)itemView.findViewById(R.id.rec_todo_priority);
            status = (TextView)itemView.findViewById(R.id.rec_todo_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.v("TodoAdapter", "OnClick");
            int adapterPos = getAdapterPosition();
            cursor.moveToPosition(adapterPos);
          //  long id = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry._ID));
           // Uri uri = TaskContract.TaskEntry.buildTaskUri(id);
            String columnIndex = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME));
            long date = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DATE));
            Uri uri = TaskContract.TaskEntry.buildUriWithNameAndDate(date,columnIndex);
            listener.onClick(uri, this);

        }
    }
    public Cursor swapCursor(Cursor newCursor) {
        Log.v("OnItemDismissS",String.valueOf(newCursor.getCount()));
     //   cursor = newCursor;
      //  notifyDataSetChanged();
        if (newCursor == cursor) {
            Log.v("OnItemDismissSR1",String.valueOf(cursor.getCount()));
            return null;
        }
        final Cursor oldCursor = cursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        cursor = newCursor;
        if (cursor != null) {
            if (mDataSetObserver != null) {
                cursor.registerDataSetObserver(mDataSetObserver);
            }
            rowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            dataIsValid = true;
            notifyDataSetChanged();

        } else {
            rowIdColumn = -1;
            dataIsValid = false;
            notifyDataSetChanged();
        }
        Log.v("OnItemDismissSR",String.valueOf(cursor.getCount()));
       // emptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
        return oldCursor;
        //emptyView.setVisibility(getItemCount()==0?View.VISIBLE:View.INVISIBLE);
    }
}
