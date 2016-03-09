package com.vishnu.flickr_sqlite.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vishnu.flickr_sqlite.App;
import com.vishnu.flickr_sqlite.FlickrContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vishnu on 3/3/16.
 */
public class PictureSyncAdapter extends AbstractThreadedSyncAdapter {

    final String TAG = getClass().getSimpleName();

    ContentResolver contentResolver;
    Context mContext;

    public PictureSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.contentResolver = context.getContentResolver();
        this.mContext = context;
    }

    public PictureSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs,
                              ContentResolver contentResolver) {
        super(context, autoInitialize, allowParallelSyncs);
        this.contentResolver = contentResolver;
        this.mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        run();
    }

    private void run() {

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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

                        mContext.getContentResolver().insert(
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
