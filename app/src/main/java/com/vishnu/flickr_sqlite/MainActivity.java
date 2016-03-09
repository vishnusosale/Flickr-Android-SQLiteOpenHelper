package com.vishnu.flickr_sqlite;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.picture_detail_frame_layout) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.picture_detail_frame_layout, new PictureDetailFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        // run();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuId = item.getItemId();

        if (menuId == R.id.update_items_menu) {
            return true;
        } else if (menuId == R.id.delete_items_menu) {

            ContentResolver contentResolver = getContentResolver();
            contentResolver.delete(FlickrContract.PictureEntry.CONTENT_URI, null, null);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
