package com.vishnu.flickr_sqlite.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by vishnu on 3/3/16.
 */
public class SyncService extends Service {

    // Object to use as a thread-safe lock
    private static final Object syncAdapterLock = new Object();
    private static PictureSyncAdapter pictureSyncAdapter = null;
    final String TAG = getClass().getSimpleName();


    @Override
    public void onCreate() {

        synchronized (syncAdapterLock) {
            if (pictureSyncAdapter == null) {
                pictureSyncAdapter = new PictureSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return pictureSyncAdapter.getSyncAdapterBinder();
    }
}
