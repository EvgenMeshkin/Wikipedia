package by.evgen.android.apiclient.helper;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.processing.NoteProcessor;
import by.evgen.android.apiclient.processing.NotesAllProcessor;
import by.evgen.android.apiclient.source.VkCachedDataSource;

/**
 * Created by evgen on 11.01.2015.
 */
public class SentsVkNotes extends OnErrorCallbacks implements ManagerDownload.Callback<List<Category>>{

    private String mBaseTitle;
    private Callbacks mCallbacks;
    private Context mContext;

    public interface Callbacks {
        void onReturnId(Long id);
    }

    public SentsVkNotes (final Callbacks callbacks, final Context context, final String title){
        super(context);
        mCallbacks = callbacks;
        mContext = context;
        mBaseTitle = title;
        ManagerDownload.load(this,
                Api.VKNOTES_ALL_GET,
                VkCachedDataSource.get(mContext),
                new NotesAllProcessor());
   }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(List<Category> data) {

        Long id = null;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getTitle().contains(mBaseTitle)) {
                id = data.get(i).getId();
            }
        }
        if (id != null) {
            mCallbacks.onReturnId(id);
            Toast.makeText(mContext, "You already added this note", Toast.LENGTH_SHORT).show();
        } else {
            ManagerDownload.load(new ManagerDownload.Callback<Long>() {
                                     @Override
                                     public void onPreExecute() {
                                     }

                                     @Override
                                     public void onPostExecute(Long data) {
                                         mCallbacks.onReturnId(data);
                                         Toast.makeText(mContext, "Note added", Toast.LENGTH_SHORT).show();
                                     }

                                     @Override
                                     public void onError(Exception e) {
                                         onErrorSent(e);
                                     }
                                 },
                    Api.getNotes(mBaseTitle),
                    VkCachedDataSource.get(mContext),
                    new NoteProcessor());
        }
    }

    private void onErrorSent (Exception e) {
        super.sentOnError(e);
    }

    @Override
    public void onError(Exception e) {
        super.sentOnError(e);
    }

}
