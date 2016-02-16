package com.vishnu.flickr_sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by vishnu on 15/2/16.
 */
public class PictureListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private final static int PICTURE_LOADER_ID = 0;
    private final String TAG = getClass().getSimpleName();
    ListView listView;
    TextView emptyView;
    PictureCursorAdapter pictureCursorAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main_list_view, container, false);

        getActivity().getSupportLoaderManager().initLoader(PICTURE_LOADER_ID, null, this);
        listView = (ListView) rootView.findViewById(R.id.list_view);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);

        listView.setEmptyView(emptyView);

        pictureCursorAdapter = new PictureCursorAdapter(getActivity().getApplicationContext(), null, 0);
        listView.setAdapter(pictureCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), PictureDetailActivity.class)
                            .setData(FlickrContract.PictureEntry.buildPictureUri(id));
                    startActivity(intent);
                }

            }
        });


        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri pictureUri = FlickrContract.PictureEntry.CONTENT_URI;
        return new CursorLoader(getActivity().getApplicationContext(),
                pictureUri,
                FlickrContract.PictureEntry.PICTURE_COLUMNS,
                null,
                null,
                FlickrContract.PictureEntry._ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        pictureCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        pictureCursorAdapter.swapCursor(null);
    }

}
