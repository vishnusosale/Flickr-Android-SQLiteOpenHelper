package com.vishnu.flickr_sqlite;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the Flickr database.
 */
public class FlickrContract {

    /**
     * The CONTENT_AUTHORITY is the name of the content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device. This will be used as ContentProvider's authority too.
     */
    public static final String CONTENT_AUTHORITY = "com.vishnu.flickr_sqlite";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible paths (appended to base content URI for possible URI's)
     * Example: content://com.vishnu.flickr_sqlite/picture
     */
    public static final String PATH_PICTURE = "picture";

    /**
     * Inner class that defines the table contents of the picture table
     */
    public static final class PictureEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_PICTURE)
                .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PICTURE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PICTURE;


        public static final String TABLE_NAME = "picture";

        public static final String COL_TITLE = "title";
        public static final String COL_LINK = "link";
        public static final String COL_IMAGE = "image";
        public static final String COL_AUTHOR = "author";
        public static final String COL_AUTHOR_ID = "author_id";

        public static Uri buildPictureUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


}
