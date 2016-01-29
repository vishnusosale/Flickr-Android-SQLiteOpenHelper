package com.vishnu.flickr_android_sqliteopenhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vishnu on 29/1/16.
 */
public class FlickrDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "flickr_android.db";
    private static final int DATABASE_VERSION = 1;

    public FlickrDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_PICTURE_TABLE = "CREATE TABLE " + FlickrContract.PictureEntry.TABLE_NAME
                + " (" +
                FlickrContract.PictureEntry.COL_TITLE + " TEXT NOT NULL, " +
                FlickrContract.PictureEntry.COL_AUTHOR + " TEXT NOT NULL, " +
                FlickrContract.PictureEntry.COL_AUTHOR_ID + " TEXT NOT NULL, " +
                FlickrContract.PictureEntry.COL_IMAGE + " TEXT NOT NULL, " +
                FlickrContract.PictureEntry.COL_LINK + " TEXT NOT NULL);";

        db.execSQL(CREATE_PICTURE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
