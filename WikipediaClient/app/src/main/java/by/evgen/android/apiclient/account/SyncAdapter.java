package by.evgen.android.apiclient.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by evgen on 05.11.2014.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private final AccountManager mAccountManager;
    private final Context mContext;

    public SyncAdapter(Context context) {
        super(context, true);
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider,
                              SyncResult syncResult) {
        String authtoken = null;
        try {
             authtoken = mAccountManager.blockingGetAuthToken(account,
                    "https://oauth.vk.com/", true);
            String login = account.name;
            String password = mAccountManager.getPassword(account);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void syncFeeds(ContentProviderClient provider, SyncResult syncResult, String where, String[] whereArgs) {

    }

    private void syncFeed(String feedId, String feedUrl, ContentProviderClient provider, SyncResult syncResult) {

    }



}
