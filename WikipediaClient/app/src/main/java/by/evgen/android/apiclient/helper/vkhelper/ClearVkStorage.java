package by.evgen.android.apiclient.helper.vkhelper;

import android.content.Context;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.db.WikiContentProvider;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.helper.OnErrorCallbacks;
import by.evgen.android.apiclient.processing.StorageSetProcessor;
import by.evgen.android.apiclient.source.VkCachedDataSource;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 25.01.2015.
 */
public class ClearVkStorage extends OnErrorCallbacks implements ManagerDownload.Callback<Long>, GetAllVkStorage.Callbacks {

    private Context mContext;
    public Boolean mRefresh;

    public ClearVkStorage(Context context) {
        super(context);
        mContext = context;
        new GetAllVkStorage(this, mContext);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(Long data) {
       if (mRefresh) {
           mContext.getContentResolver().delete(WikiContentProvider.WIKI_STORAGE_URI, null, null);
       }
   }

    @Override
    public void onError(Exception e) {
        onErrorSent(e);
    }

    private void onErrorSent(Exception e){
        super.sentOnError(e);
    }

    @Override
    public void onAllVkStorage(List<String> data) {
        Log.d(getClass(), "Storage" + data);
        for (int i = 0; i < data.size(); i++) {
            ManagerDownload.load(this,
                    Api.STORAGE_SET + data.get(i) +"&value=",
                    VkCachedDataSource.get(mContext),
                    new StorageSetProcessor());
            if (i == data.size()-1) {
                mRefresh = true;
            }
        }
    }

}

