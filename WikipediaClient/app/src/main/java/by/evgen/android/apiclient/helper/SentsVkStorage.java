package by.evgen.android.apiclient.helper;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.processing.StorageSetProcessor;
import by.evgen.android.apiclient.source.VkCachedDataSource;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 21.01.2015.
 */
public class SentsVkStorage extends OnErrorCallbacks implements ManagerDownload.Callback<Long>, GetAllVkStorage.Callbacks {

    private String mBaseUrl;
    private Context mContext;

    public SentsVkStorage(final Context context, final String url) {
        super(context);
        mContext = context;
        mBaseUrl = url;
        Log.text(this.getClass(), "Url " + Api.STORAGE_SET + mBaseUrl +"&value=" + mBaseUrl);
        new GetAllVkStorage(this, mContext);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(Long data) {
         Toast.makeText(mContext, "Note added", Toast.LENGTH_SHORT).show();
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
        String pageName = Constant.getEmpty();
        Log.text(getClass(), "Storage" + data);
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).contains(mBaseUrl)) {
                pageName = data.get(i);
            }
        }
        if (!pageName.equals(Constant.getEmpty())) {
            Toast.makeText(mContext, "You already added this note", Toast.LENGTH_SHORT).show();
        } else {
            ManagerDownload.load(this,
                    Api.getStorage(mBaseUrl),
                    VkCachedDataSource.get(mContext),
                    new StorageSetProcessor());
            Log.text(getClass(), Api.getStorage(mBaseUrl));
        }
    }
}

