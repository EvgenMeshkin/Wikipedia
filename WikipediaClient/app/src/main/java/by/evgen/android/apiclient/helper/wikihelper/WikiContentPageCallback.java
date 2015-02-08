package by.evgen.android.apiclient.helper.wikihelper;

import android.content.Context;

import java.util.List;

import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.helper.OnErrorCallbacks;
import by.evgen.android.apiclient.processing.ContentsArrayProcessor;
import by.evgen.android.apiclient.source.HttpDataSource;

/**
 * Created by evgen on 16.01.2015.
 */
public class WikiContentPageCallback extends OnErrorCallbacks implements ManagerDownload.Callback<List<Category>> {

    private Callbacks mCallback;

    public WikiContentPageCallback (Context context, Callbacks callbacks, String url){
        super(context);
        mCallback = callbacks;
        ManagerDownload.load(this, url, new HttpDataSource(), new ContentsArrayProcessor());

    }

    public interface Callbacks {
        void onSetContents(List<Category> data);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(List<Category> data) {
        mCallback.onSetContents(data);
    }

    @Override
    public void onError(Exception e) {
        super.sentOnError(e);
    }

}
