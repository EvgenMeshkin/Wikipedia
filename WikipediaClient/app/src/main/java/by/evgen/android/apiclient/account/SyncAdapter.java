package by.evgen.android.apiclient.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import java.io.InputStream;
import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.auth.Authorized;
import by.evgen.android.apiclient.helper.GetAllVkStorage;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.processing.StorageGetKeysProcessor;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 05.11.2014.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter implements GetAllVkStorage.Callbacks{

    private final AccountManager mAccountManager;
    private final Context mContext;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider,
                              SyncResult syncResult) {
        Log.text(getClass(), "Start sync adapter" + Authorized.isLogged());
        try {
        //TODO
        Authorized.setLogged(true);
        if (Authorized.isLogged()) {
                StorageGetKeysProcessor processor = new StorageGetKeysProcessor(mContext);
                InputStream dataUrl = VkDataSource.get(mContext).getResult(Api.STORAGE_KEYS_GET);
                processor.process(dataUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncFeeds(ContentProviderClient provider, SyncResult syncResult, String where, String[] whereArgs) {

    }

    private void syncFeed(String feedId, String feedUrl, ContentProviderClient provider, SyncResult syncResult) {

    }


    @Override
    public void onAllVkStorage(List<String> data) {

    }
}
