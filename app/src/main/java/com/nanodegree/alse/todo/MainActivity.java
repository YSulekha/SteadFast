package com.nanodegree.alse.todo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nanodegree.alse.todo.Adapter.RecyclerAdapter;
import com.nanodegree.alse.todo.data.TaskContract;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerAdapter mRecyclerAdapter;
    static final int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        TextView textView = (TextView)findViewById(R.id.recyclerview_emptyView);
        mRecyclerAdapter = new RecyclerAdapter(this,textView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mRecyclerAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);

        //Add notes button
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.add_todo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,DialogActivity.class);
                intent.putExtra(DialogActivity.ISEDIT,"false");
                startActivity(intent);
            }
        });

        //Set the title when the appbar is collapsed
        final CollapsingToolbarLayout c = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.appBar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int onScroll =-1;
            boolean show = false;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(onScroll == -1){
                    onScroll = appBarLayout.getTotalScrollRange();
                }
                if(verticalOffset+onScroll ==0){
                    c.setTitle(getString(R.string.app_name));
                    show = true;
                }
                else if(show){
                    c.setTitle(" ");
                }
            }
        });

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

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = TaskContract.TaskEntry.CONTENT_URI;
        String[] projection = new String[]{"DISTINCT "+TaskContract.TaskEntry.COLUMN_TASK_DATE};
        String sortOrder = TaskContract.TaskEntry.COLUMN_TASK_DATE + " ASC";
        return new android.content.CursorLoader(this,uri,projection,null,null,sortOrder);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        if(data != null){
            mRecyclerAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mRecyclerAdapter.swapCursor(null);
    }



}
