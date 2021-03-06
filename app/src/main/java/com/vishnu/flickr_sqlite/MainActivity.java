package com.vishnu.flickr_sqlite;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vishnu.flickr_sqlite.sync.PictureSyncAdapter;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    boolean mTwoPane;
    PictureSyncAdapter mPictureSyncAdapter;

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
            run();
            return true;
        } else if (menuId == R.id.delete_items_menu) {

            ContentResolver contentResolver = getContentResolver();
            contentResolver.delete(FlickrContract.PictureEntry.CONTENT_URI, null, null);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void run() {

        mPictureSyncAdapter = new PictureSyncAdapter(getApplicationContext(), true);
        mPictureSyncAdapter.syncImmediately(getApplicationContext());

//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//                App.FLICKR_API_ENDPOINT, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//
//                    for (int i = 0; i < response.getJSONArray("items").length(); i++) {
//                        ContentValues contentValues = new ContentValues();
//
//                        contentValues.put(FlickrContract.PictureEntry.COL_TITLE,
//                                response.getJSONArray("items").getJSONObject(i).getString("title"));
//                        contentValues.put(FlickrContract.PictureEntry.COL_AUTHOR,
//                                response.getJSONArray("items").getJSONObject(i).getString("author"));
//                        contentValues.put(FlickrContract.PictureEntry.COL_AUTHOR_ID,
//                                response.getJSONArray("items").getJSONObject(i).getString("author_id"));
//                        contentValues.put(FlickrContract.PictureEntry.COL_LINK,
//                                response.getJSONArray("items").getJSONObject(i).getString("link"));
//                        contentValues.put(FlickrContract.PictureEntry.COL_IMAGE,
//                                response.getJSONArray("items").getJSONObject(i).getJSONObject("media")
//                                        .getString("m"));
//                        contentValues.put(FlickrContract.PictureEntry.COL_PUBLISHED_DATE,
//                                response.getJSONArray("items").getJSONObject(i).getString("published"));
//
//                        getContentResolver().insert(
//                                FlickrContract.PictureEntry.CONTENT_URI,
//                                contentValues
//                        );
//                    }
//
////                    Uri insertedUri = getApplicationContext().getContentResolver().insert(
////                            FlickrContract.PictureEntry.CONTENT_URI,
////                            contentValues);
//
////                    long id = ContentUris.parseId(insertedUri);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "volleyError", error);
//            }
//        });
//
//        requestQueue.add(jsonObjectRequest);
    }
}
