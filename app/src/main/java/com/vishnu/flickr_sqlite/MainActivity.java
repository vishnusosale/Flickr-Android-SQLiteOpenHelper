package com.vishnu.flickr_sqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static int PICTURE_LOADER_ID = 0;
    private final String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    ListView listView;
    PictureCursorAdapter pictureCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportLoaderManager().initLoader(PICTURE_LOADER_ID, null, this);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        listView = (ListView) findViewById(R.id.list_view);

        setSupportActionBar(toolbar);
        run();

        pictureCursorAdapter = new PictureCursorAdapter(getApplicationContext(), null, 0);
        listView.setAdapter(pictureCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if (cursor != null) {
                    Intent intent = new Intent(MainActivity.this, ImageDetailActivity.class)
                            .setData(FlickrContract.PictureEntry.buildPictureUri(id));
                    startActivity(intent);
                }

            }
        });

    }

    private void run() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                App.FLICKR_API_ENDPOINT, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    for (int i = 0; i < response.getJSONArray("items").length(); i++) {
                        ContentValues contentValues = new ContentValues();

                        contentValues.put(FlickrContract.PictureEntry.COL_TITLE,
                                response.getJSONArray("items").getJSONObject(i).getString("title"));
                        contentValues.put(FlickrContract.PictureEntry.COL_AUTHOR,
                                response.getJSONArray("items").getJSONObject(i).getString("author"));
                        contentValues.put(FlickrContract.PictureEntry.COL_AUTHOR_ID,
                                response.getJSONArray("items").getJSONObject(i).getString("author_id"));
                        contentValues.put(FlickrContract.PictureEntry.COL_LINK,
                                response.getJSONArray("items").getJSONObject(i).getString("link"));
                        contentValues.put(FlickrContract.PictureEntry.COL_IMAGE,
                                response.getJSONArray("items").getJSONObject(i).getJSONObject("media")
                                        .getString("m"));
                        contentValues.put(FlickrContract.PictureEntry.COL_PUBLISHED_DATE,
                                response.getJSONArray("items").getJSONObject(i).getString("published"));

                        getApplicationContext().getContentResolver().insert(
                                FlickrContract.PictureEntry.CONTENT_URI,
                                contentValues
                        );
                    }

//                    Uri insertedUri = getApplicationContext().getContentResolver().insert(
//                            FlickrContract.PictureEntry.CONTENT_URI,
//                            contentValues);

//                    long id = ContentUris.parseId(insertedUri);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "volleyError", error);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri pictureUri = FlickrContract.PictureEntry.CONTENT_URI;
        return new CursorLoader(getApplicationContext(),
                pictureUri,
                FlickrContract.PictureEntry.PICTURE_COLUMNS,
                null,
                null,
                FlickrContract.PictureEntry._ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        pictureCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        pictureCursorAdapter.swapCursor(null);
    }
}
