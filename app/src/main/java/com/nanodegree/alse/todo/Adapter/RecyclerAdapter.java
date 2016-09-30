package com.nanodegree.alse.todo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.alse.todo.DialogActivity;
import com.nanodegree.alse.todo.R;
import com.nanodegree.alse.todo.data.TaskContract;
import com.nanodegree.alse.todo.data.Utility;

/**
 * Created by aharyadi on 9/21/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.DateViewHolder> {


    Context context;
    Cursor cursor;

    public RecyclerAdapter(Context c){
        context = c;
    }
    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_item,viewGroup,false);
        DateViewHolder d = new DateViewHolder(view);
        return d;
    }

    @Override
    public void onBindViewHolder(DateViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);
        long date = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DATE));
        Log.v("RecyclerAdapterBind",String.valueOf(date));
        viewHolder.day.setText(Utility.getDayName(Long.toString(date)));
        setTodoAdapter(viewHolder,date);

    }

    public void setTodoAdapter(DateViewHolder viewHolder, long date){
        Uri uri = TaskContract.TaskEntry.buildUriWithDate(date);
        Log.v("RecyclerAdapterBind", uri.toString());
        Cursor cur = context.getContentResolver().query(uri, null, null, null, null, null);
        Log.v("OnItemDismissR",String.valueOf(cur.getCount()));
        TodoRecyclerAdapter adapter = new TodoRecyclerAdapter(context, new TodoRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onClick(Uri uri, TodoRecyclerAdapter.TodoViewHolder vh) {
                Log.v("InsideRecycle", uri.toString());
                Intent intent = new Intent(context,DialogActivity.class);
                intent.setData(uri);
                intent.putExtra(DialogActivity.ISEDIT, "true");
                context.startActivity(intent);
                Log.v("onclick","Adapter");
            }
        },cur);
        viewHolder.rec.setAdapter(adapter);
        viewHolder.rec.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper.Callback callback = new ItemSwipeCallback(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(viewHolder.rec);
        adapter.swapCursor(cur);
    }

    @Override
    public int getItemCount() {

        if(null==cursor)
            return 0;
        Log.v("RecyclerAdapterCount",String.valueOf(cursor.getCount()));
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
        //emptyView.setVisibility(getItemCount()==0?View.VISIBLE:View.INVISIBLE);
    }

    public class DateViewHolder extends RecyclerView.ViewHolder{

        TextView day;
        RecyclerView rec;

        public DateViewHolder(View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.rec_day);
            rec = (RecyclerView)itemView.findViewById(R.id.rec_todo);
        }
    }
}
