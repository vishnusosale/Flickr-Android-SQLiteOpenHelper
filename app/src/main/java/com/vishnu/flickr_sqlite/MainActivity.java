package com.vishnu.flickr_sqlite;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.picture_detail_frame_layout) != null)
        {
            mTwoPane = true;
            if (savedInstanceState == null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.picture_detail_frame_layout, new PictureDetailFragment())
                        .commit();
            }
        }
        else
        {
            mTwoPane = false;
        }
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        run();

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


}
