package com.nanodegree.alse.todo.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.alse.todo.R;
import com.nanodegree.alse.todo.data.TaskContract;

//Adapter for recycler view for displaying to-do list
public class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.TodoViewHolder> {

    Cursor cursor;
    Context context;
    private OnItemClickListener listener;
    private TextView emptyView;

    public static interface OnItemClickListener {
        public void onClick(Uri uri, TodoViewHolder vh);
    }

    public TodoRecyclerAdapter(Context c, OnItemClickListener l) {
        context = c;
        listener = l;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_listitem, viewGroup, false);
        TodoViewHolder viewHolder = new TodoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodoViewHolder todoViewHolder, int i) {
        cursor.moveToPosition(i);
        todoViewHolder.todo.setText(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME)));
        todoViewHolder.priority.setText(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_PRIORITY)));
        todoViewHolder.status.setText(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_STATUS)));

    }

    @Override
    public int getItemCount() {
        if (null == cursor)
            return 0;
        return cursor.getCount();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView todo;
        TextView priority;
        TextView status;

        public TodoViewHolder(View itemView) {
            super(itemView);
            todo = (TextView) itemView.findViewById(R.id.rec_todo_name);
            priority = (TextView) itemView.findViewById(R.id.rec_todo_priority);
            status = (TextView) itemView.findViewById(R.id.rec_todo_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPos = getAdapterPosition();
            cursor.moveToPosition(adapterPos);
            String columnIndex = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME));
            long date = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DATE));
            Uri uri = TaskContract.TaskEntry.buildUriWithNameAndDate(date, columnIndex);
            listener.onClick(uri, this);
        }
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }
}
