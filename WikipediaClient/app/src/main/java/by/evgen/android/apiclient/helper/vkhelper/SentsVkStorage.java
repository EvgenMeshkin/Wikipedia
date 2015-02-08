package by.evgen.android.apiclient.helper.vkhelper;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.db.WikiContentProvider;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.helper.OnErrorCallbacks;
import by.evgen.android.apiclient.processing.StorageSetProcessor;
import by.evgen.android.apiclient.source.VkCachedDataSource;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 21.01.2015.
 */
public class SentsVkStorage extends OnErrorCallbacks implements ManagerDownload.Callback<Long>, GetAllVkStorage.Callbacks {

    private String mKey;
    private Context mContext;
    private String mValue;

    public SentsVkStorage(final Context context, final String key, String value) {
        super(context);
        mContext = context;
        mKey = key;
        mValue = value;
        Log.d(this.getClass(), "Url " + Api.STORAGE_SET + mKey + "&value=" + mKey);
        new GetAllVkStorage(this, mContext);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(Long data) {
        Toast.makeText(mContext, "Note added", Toast.LENGTH_SHORT).show();
        Account vkAccount = new Account(mContext.getString(R.string.acount_name), Constant.ACCOUNT_TYPE);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(vkAccount, WikiContentProvider.AUTHORITY, bundle);
    }

    @Override
    public void onError(Exception e) {
        onErrorSent(e);
    }

    private void onErrorSent(Exception e) {
        super.sentOnError(e);
    }

    @Override
    public void onAllVkStorage(List<String> data) {
        String pageName = Constant.EMPTY;
        Log.d(getClass(), "Storage" + data);
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).contains(mKey)) {
                pageName = data.get(i);
            }
        }
        if (!pageName.equals(Constant.EMPTY)) {
            Toast.makeText(mContext, "You already added this note", Toast.LENGTH_SHORT).show();
        } else {
            ManagerDownload.load(this,
                    Api.getStorage(mKey, mValue),
                    VkCachedDataSource.get(mContext),
                    new StorageSetProcessor());
            Log.d(getClass(), Api.getStorage(mKey, mValue));
        }
    }

}

