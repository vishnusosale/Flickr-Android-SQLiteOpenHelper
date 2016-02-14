package com.vishnu.flickr_sqlite;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getApplicationContext(),
                uri,
                FlickrContract.PictureEntry.PICTURE_COLUMNS,
                FlickrContract.PictureEntry._ID + " IS ? ",
                new String[]{uri.getLastPathSegment()},
                null
        );

//        return new CursorLoader(
//                getApplicationContext(),
//                uri,
//                FlickrContract.PictureEntry.PICTURE_COLUMNS,
//                null,
//                null,
//                FlickrContract.PictureEntry._ID + " DESC"
//        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (!cursor.moveToFirst()) {
            return;
        }
        Uri pictureUri = Uri.parse(cursor.getString(FlickrContract.PictureEntry.COL_PICTURE_IMAGE));
        long id = cursor.getLong(FlickrContract.PictureEntry.COL_PICTURE_ID);

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(pictureUri)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(picture_image_view.getController())
                .build();

        picture_image_view.setController(controller);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
