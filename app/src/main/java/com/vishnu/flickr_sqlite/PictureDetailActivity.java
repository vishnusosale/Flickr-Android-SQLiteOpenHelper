package com.vishnu.flickr_sqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PictureDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.picture_detail_frame_layout, new PictureDetailFragment())
                    .commit();
        }
    }
}
