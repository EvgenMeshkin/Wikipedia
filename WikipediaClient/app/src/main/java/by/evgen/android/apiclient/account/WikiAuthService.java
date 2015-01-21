package by.evgen.android.apiclient.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import by.evgen.android.apiclient.auth.VkOAuthHelper;

/**
 * Created by evgen on 22.01.2015.
 */
public class WikiAuthService extends Service {

    private WikiAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuthenticator = new WikiAuthenticator(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

}
