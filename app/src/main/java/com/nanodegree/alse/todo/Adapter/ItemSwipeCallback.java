package com.nanodegree.alse.todo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by aharyadi on 9/29/16.
 */
public class ItemSwipeCallback extends ItemTouchHelper.Callback {

    public ItemTouchHelpAdapter adapter;

    public ItemSwipeCallback(ItemTouchHelpAdapter ad){
        adapter=ad;
    }

    @Override
    public boolean isItemViewSwipeEnabled(){
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int swipefalgs = ItemTouchHelper.START|ItemTouchHelper.END|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
        return makeMovementFlags(0,swipefalgs);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.v("Inside OnSwiped","helper");
        Log.v("Inside OnSwiped",String.valueOf(viewHolder.getItemId()));
       adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
