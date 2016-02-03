package com.vishnu.flickr_sqlite;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by vishnu on 30/1/16.
 */
public class PictureCursorAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_PICTURE_LIST = 0;
    private static final int VIEW_TYPE_COUNT = 1;

    public PictureCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int viewType = getItemViewType(cursor.getPosition());
        int mLayoutId = -1;

        switch (viewType) {
            case VIEW_TYPE_PICTURE_LIST:
                mLayoutId = R.layout.picture_item;
                break;
        }

        View view = LayoutInflater.from(context).inflate(mLayoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_PICTURE_LIST;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_PICTURE_LIST:
                Uri pictureUri = Uri.parse(cursor.getString(FlickrContract.PictureEntry.COL_PICTURE_IMAGE));
//                Picasso.with(context)
//                        .load(pictureUri)
//                        .placeholder(R.drawable.placeholder)
//                        .into(viewHolder.imageView);

                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(pictureUri)
                        .setProgressiveRenderingEnabled(true)
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(viewHolder.imageView.getController())
                        .build();

                viewHolder.imageView.setController(controller);
                viewHolder.titleTextView.setText(cursor.getString(FlickrContract.PictureEntry.COL_PICTURE_TITLE));
        }
    }

    public static class ViewHolder {
        public final TextView titleTextView;
        public final SimpleDraweeView imageView;

        public ViewHolder(View view) {
            titleTextView = (TextView) view.findViewById(R.id.picture_title);
            imageView = (SimpleDraweeView) view.findViewById(R.id.picture_image_view);
        }
    }
}
