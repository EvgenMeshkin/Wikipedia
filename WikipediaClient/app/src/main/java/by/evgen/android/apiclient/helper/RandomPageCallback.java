package by.evgen.android.apiclient.helper;



import android.content.Context;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.processing.RandomProcessor;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.utils.Log;

import java.util.List;

/**
 * Created by User on 05.01.2015.
 */
public class RandomPageCallback extends OnErrorCallbacks implements ManagerDownload.Callback<List<Category>>{

    private Callbacks mCallback;

    public interface Callbacks {
        void onShowDetails(NoteGsonModel note);
     }

    public RandomPageCallback (Context context, Callbacks callback){
        super(context);
        Log.text(this.getClass(), "StartLoader");
        mCallback = callback;
        ManagerDownload.load(this,
                Api.RANDOM_GET,
                new HttpDataSource(),
                new RandomProcessor());
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(List<Category> data) {
      Integer i = data.size();
      Category item = data.get(i-1);
      NoteGsonModel note = new NoteGsonModel(null, item.getTitle(), null);
      mCallback.onShowDetails(note);
    }

    @Override
    public void onError(Exception e) {
        Log.text(this.getClass(), "Error" );
        super.sentOnError(e);
    }

}
