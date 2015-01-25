package by.evgen.android.apiclient.helper;

import android.content.Context;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.processing.StorageGetKeysProcessor;
import by.evgen.android.apiclient.source.VkDataSource;

/**
 * Created by evgen on 25.01.2015.
 */
public class GetAllVkStorage extends OnErrorCallbacks implements ManagerDownload.Callback<List<String>> {

    private Callbacks mCallbacks;

    public GetAllVkStorage(Callbacks callbacks, Context context) {
        super(context);
        mCallbacks = callbacks;
        ManagerDownload.load(this,
                Api.STORAGE_KEYS_GET,
                VkDataSource.get(context),
                new StorageGetKeysProcessor());
    }

    public interface Callbacks {
        void onAllVkStorage(List<String> data);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(List<String> data) {
        mCallbacks.onAllVkStorage(data);
    }

    @Override
    public void onError(Exception e) {
         super.sentOnError(e);
    }
}
