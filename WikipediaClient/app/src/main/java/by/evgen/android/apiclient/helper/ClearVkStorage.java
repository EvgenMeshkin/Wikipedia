package by.evgen.android.apiclient.helper;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.auth.VkOAuthHelper;
import by.evgen.android.apiclient.processing.StorageSetProcessor;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 25.01.2015.
 */
public class ClearVkStorage extends OnErrorCallbacks implements ManagerDownload.Callback<Long>, GetAllVkStorage.Callbacks {

    private Context mContext;

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
        Toast.makeText(mContext, "Note delete", Toast.LENGTH_SHORT).show();
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
        Log.text(getClass(), "Storage" + data);
        for (int i = 0; i < data.size(); i++) {
            ManagerDownload.load(this,
                    Api.STORAGE_SET + data.get(i) +"&value=",
                    VkDataSource.get(mContext),
                    new StorageSetProcessor());
        }
    }

}

