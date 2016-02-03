package com.vishnu.flickr_sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vishnu on 29/1/16.
 */
public class FlickrDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "flickr_android.db";
    private static final int DATABASE_VERSION = 2;

    public FlickrDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_PICTURE_TABLE = "CREATE TABLE " + FlickrContract.PictureEntry.TABLE_NAME
                + " (" +
                FlickrContract.PictureEntry._ID + " INTEGER PRIMARY KEY , " +
                FlickrContract.PictureEntry.COL_TITLE + " TEXT , " +
                FlickrContract.PictureEntry.COL_AUTHOR + " TEXT , " +
                FlickrContract.PictureEntry.COL_AUTHOR_ID + " TEXT , " +
                FlickrContract.PictureEntry.COL_IMAGE + " TEXT , " +
                FlickrContract.PictureEntry.COL_LINK + " TEXT , " +
                FlickrContract.PictureEntry.COL_PUBLISHED_DATE + " TEXT);";

        db.execSQL(CREATE_PICTURE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        This database is only a cache for online data, so its upgrade policy is
        to simply to discard the data and start over
         */

        db.execSQL("DROP TABLE IF EXISTS " + FlickrContract.PictureEntry.TABLE_NAME);
        onCreate(db);
    }
}
