package by.evgen.android.apiclient.adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import java.io.InputStream;
import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.auth.Authorized;
import by.evgen.android.apiclient.helper.vkhelper.GetAllVkStorage;
import by.evgen.android.apiclient.processing.StorageGetKeysProcessor;
import by.evgen.android.apiclient.processing.StorageGetProcessor;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 05.11.2014.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter implements GetAllVkStorage.Callbacks {

    private final Context mContext;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider,
                              SyncResult syncResult) {
        Log.d(getClass(), "Start sync adapter" + Authorized.isLogged());
        if (account != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Authorized.setLogged(true);
                        StorageGetKeysProcessor processor = new StorageGetKeysProcessor(mContext);
                        InputStream dataUrl = VkDataSource.get(mContext).getResult(Api.STORAGE_KEYS_GET);
                        List<String> noteArray = processor.process(dataUrl);
                        String str = Constant.EMPTY;
                        for (int i = 0; i < noteArray.size(); i++) {
                            str = str + noteArray.get(i) + ",";
                        }
                        InputStream valueUrl = VkDataSource.get(mContext).getResult(Api.STORAGE_GET + str);
                        StorageGetProcessor getProcessor = new StorageGetProcessor(mContext);
                        getProcessor.process(valueUrl);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    @Override
    public void onAllVkStorage(List<String> data) {
    }

}
