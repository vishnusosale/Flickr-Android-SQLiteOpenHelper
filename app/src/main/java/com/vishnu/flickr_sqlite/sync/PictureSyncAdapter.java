package com.vishnu.flickr_sqlite.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import com.vishnu.flickr_sqlite.App;
import com.vishnu.flickr_sqlite.FlickrContract;
import com.vishnu.flickr_sqlite.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vishnu on 29/3/16.
 */
public class PictureSyncAdapter extends AbstractThreadedSyncAdapter {

    private final String TAG = getClass().getSimpleName();
    private Context mContext;

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    public PictureSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.mContext = context;
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public void syncImmediately(Context context) {

        Log.e("syncImmediately", "syncImmediately");


        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                FlickrContract.CONTENT_AUTHORITY, bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), "vishnu.flickr.com");

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        }
        return newAccount;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.e(TAG, "onPerformSync Called.");

        RequestQueue queue = VolleyService.getInstance(this.getContext()).getRequestQueue();
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

        queue.add(jsonObjectRequest);

    }
}
