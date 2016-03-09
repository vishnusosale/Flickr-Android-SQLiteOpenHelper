package com.vishnu.flickr_sqlite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by vishnu on 30/1/16.
 */
public class PictureProvider extends ContentProvider {


    static final int PICTURE = 100;
    static final int PICTURE_WITH_ID = 101;
    FlickrDbHelper flickrDbHelper;
    UriMatcher uriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FlickrContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, FlickrContract.PATH_PICTURE, PICTURE);
        matcher.addURI(authority, FlickrContract.PATH_PICTURE + "/*", PICTURE_WITH_ID);

        return matcher;

    }

    @Override
    public boolean onCreate() {
        flickrDbHelper = new FlickrDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor cursor;
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        final int match = uriMatcher.match(uri);

        if (match == PICTURE) {
            cursor = flickrDbHelper
                    .getReadableDatabase()
                    .query(FlickrContract.PictureEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
        } else if (match == PICTURE_WITH_ID) {
            cursor = flickrDbHelper
                    .getReadableDatabase()
                    .query(FlickrContract.PictureEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        /**
         * set notificationUri for the cursor to the one that was passed into the function
         * The cursor registers the content observer to watch for changes to that uri and any of its
         * descendants to notify changes on update or insert.
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Key information getType conveys is whether the content uri returning a cursor is of type
     * directory or type item.
     *
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(Uri uri) {

        /**
         * User UriMatcher to determine type of uri.
         */
        final int match = uriMatcher.match(uri);

        switch (match) {
            case PICTURE:
                return FlickrContract.PictureEntry.CONTENT_TYPE;
            case PICTURE_WITH_ID:
                return FlickrContract.PictureEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = flickrDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {

            case PICTURE:
                // insert returns the row id of the inserted row, -1 if there's an error
                long _id = db.insert(FlickrContract.PictureEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = FlickrContract.PictureEntry.buildPictureUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = flickrDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if (selection == null) {
            selection = "1";
        }

        switch (match) {
            case PICTURE:
                rowsDeleted = db.delete(FlickrContract.PictureEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = flickrDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case PICTURE:
                rowsUpdated = db.update(FlickrContract.PictureEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
