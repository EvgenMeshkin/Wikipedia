package by.evgen.android.apiclient.helper;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.auth.VkOAuthHelper;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.processing.NoteProcessor;
import by.evgen.android.apiclient.processing.NotesAllProcessor;
import by.evgen.android.apiclient.processing.StorageGetKeysProcessor;
import by.evgen.android.apiclient.processing.StorageSetProcessor;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 21.01.2015.
 */
public class SentsVkStorage implements ManagerDownload.Callback<List<String>> {

    private String mBaseUrl;
    private Callbacks mCallbacks;
    private Context mContext;
    final String LOG_TAG = getClass().getSimpleName();


    public interface Callbacks {
        void onReturnId(Long id);
    }

    public SentsVkStorage(final Context context, final String url) {
//        mCallbacks = callbacks;
        android.util.Log.d(LOG_TAG, "Sent storage" + VkOAuthHelper.mAccessToken );
        Log.text(this.getClass(), "Url " + VkOAuthHelper.mAccessToken);
        mContext = context;
        mBaseUrl = url;
        Log.text(this.getClass(), "Url " + Api.STORAGE_SET + mBaseUrl +"&value=" + mBaseUrl);
        ManagerDownload.load(this,
                Api.STORAGE_KEYS_GET,
                new VkDataSource(),
                new StorageGetKeysProcessor());

    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(List<String> data) {
        android.util.Log.d(LOG_TAG, data.toString());
        String pageName = "";
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).indexOf(mBaseUrl) != -1) {
                pageName = data.get(i);
            }
        }
        if (pageName != "") {
       //     mCallbacks.onReturnId(id);
            Toast.makeText(mContext, "You already added this note", Toast.LENGTH_SHORT).show();
        } else {
            ManagerDownload.load(new ManagerDownload.Callback<Long>() {

                                     @Override
                                     public void onPreExecute() {
                                     }

                                     @Override
                                     public void onPostExecute(Long data) {
                                   //      mCallbacks.onReturnId(data);
                                         android.util.Log.d(LOG_TAG, data.toString());
                                         Toast.makeText(mContext, "Note added", Toast.LENGTH_SHORT).show();
                                     }

                                     @Override
                                     public void onError(Exception e) {
//                                         e.printStackTrace();
                                         onError(e);
                                     }
                                 },
                    Api.STORAGE_SET + mBaseUrl +"&value=" + mBaseUrl,
                    new VkDataSource(),
                    new StorageSetProcessor());
        }
    }

    @Override
    public void onError(Exception e) {

    }

}

