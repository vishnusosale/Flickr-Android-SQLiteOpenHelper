package com.vishnu.flickr_sqlite;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;

public class ImageDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;

    SimpleDraweeView picture_image_view;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        picture_image_view = (SimpleDraweeView) findViewById(R.id.picture_image_view);

        if (savedInstanceState == null) {
            uri = getIntent().getData();
        }

        getSupportLoaderManager().initLoader(DETAIL_LOADER, null, ImageDetailActivity.this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != uri) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getApplicationContext(),
                    uri,
                    FlickrContract.PictureEntry.PICTURE_COLUMNS,
                    null,
                    null,
                    FlickrContract.PictureEntry._ID + " DESC"
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {
            int pictureId = data.getInt(FlickrContract.PictureEntry.COL_PICTURE_ID);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
