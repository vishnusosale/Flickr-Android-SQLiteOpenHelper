package com.vishnu.flickr_sqlite.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by vishnu on 29/3/16.
 */
public class PictureAuthenticatorService extends Service {

    private PictureAuthenticator mPictureAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        mPictureAuthenticator = new PictureAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mPictureAuthenticator.getIBinder();
    }
}
