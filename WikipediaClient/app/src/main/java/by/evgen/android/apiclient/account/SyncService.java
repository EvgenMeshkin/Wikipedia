package by.evgen.android.apiclient.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 05.11.2014.
 */
//TODO refactoring or read about that
public class SyncService extends Service {

    private static SyncAdapter sSyncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sSyncAdapter == null) {
            synchronized (SyncAdapter.class) {
                Log.text(getClass(), "Start sync servise");
                sSyncAdapter = new SyncAdapter(getApplicationContext());
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }

}