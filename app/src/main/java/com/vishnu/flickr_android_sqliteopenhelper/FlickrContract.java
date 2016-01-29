package com.vishnu.flickr_android_sqliteopenhelper;

import android.provider.BaseColumns;

/**
 * Defines table and column names for the Flickr database.
 */
public class FlickrContract {


    /**
     *
     */
    public static final class PictureEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "picture";

        public static final String COL_TITLE = "title";
        public static final String COL_LINK = "link";
        public static final String COL_IMAGE = "image";
        public static final String COL_AUTHOR = "author";
        public static final String COL_AUTHOR_ID = "author_id";

    }
}
