package com.vishnu.flickr_sqlite;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by vishnu on 15/2/16.
 */
public class PictureDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;

    SimpleDraweeView picture_image_view;
    TextView picture_title;
    Uri uri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_picture_detail, container, false);
        picture_image_view = (SimpleDraweeView) rootView.findViewById(R.id.picture_image_view);
        picture_title = (TextView) rootView.findViewById(R.id.picture_title);

        if (savedInstanceState == null) {
            uri = getActivity().getIntent().getData();
        }

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity().getApplicationContext(),
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

        picture_title.setText(cursor.getString(FlickrContract.PictureEntry.COL_PICTURE_TITLE));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
