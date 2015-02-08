package by.evgen.android.apiclient.helper.wikihelper;

import android.content.Context;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.helper.OnErrorCallbacks;
import by.evgen.android.apiclient.processing.IdAndTitleProcessor;
import by.evgen.android.apiclient.source.HttpDataSource;

/**
 * Created by evgen on 06.02.2015.
 */
public class WikiGetIdTitle extends OnErrorCallbacks implements ManagerDownload.Callback<List<Category>> {

    private Callbacks mCallback;

    public WikiGetIdTitle (Context context, Callbacks callbacks, String url){
        super(context);
        mCallback = callbacks;
        ManagerDownload.load(this, Api.IDTITLE_GET + url, new HttpDataSource(), new IdAndTitleProcessor());

    }

    public interface Callbacks {
        void onSetIdTitle(List<Category> data);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(List<Category> data) {
        mCallback.onSetIdTitle(data);
    }

    @Override
    public void onError(Exception e) {
        super.sentOnError(e);
    }

}

