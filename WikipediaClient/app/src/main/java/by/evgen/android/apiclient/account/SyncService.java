package by.evgen.android.apiclient.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 05.11.2014.
 */
public class SyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter sSyncAdapter;

    @Override
    public void onCreate() {
        Log.text(getClass(), "Start sync servise");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null)
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }

}