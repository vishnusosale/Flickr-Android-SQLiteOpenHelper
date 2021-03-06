package com.vishnu.flickr_sqlite;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by vishnu on 29/1/16.
 */
public class App extends Application {

    public static final String FLICKR_API_ENDPOINT
            = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
