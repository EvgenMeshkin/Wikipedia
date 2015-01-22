package by.evgen.android.apiclient.helper;

import java.util.List;

import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.processing.ContentsArrayProcessor;
import by.evgen.android.apiclient.source.HttpDataSource;

/**
 * Created by evgen on 16.01.2015.
 */
public class WikiContentPageCallback implements ManagerDownload.Callback<List<Category>>{

    private Callbacks mCallback;

    public WikiContentPageCallback (Callbacks callbacks, String url){
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

    }

}
