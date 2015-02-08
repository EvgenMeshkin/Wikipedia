package by.evgen.android.apiclient.helper.vkhelper;

import android.content.Context;
import android.widget.Toast;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.helper.OnErrorCallbacks;
import by.evgen.android.apiclient.processing.LikeIsProcessor;
import by.evgen.android.apiclient.source.VkCachedDataSource;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 12.01.2015.
 */
public class LikeVkNotes extends OnErrorCallbacks implements SentsVkNotes.Callbacks{

    private Context mContext;

    public LikeVkNotes (Context context, String url){
        super(context);
        mContext = context;
        new SentsVkNotes(this, context, url);
    }

    @Override
    public void onReturnId(final Long id) {
        ManagerDownload.load(new ManagerDownload.Callback<Integer>() {

                                 @Override
                                 public void onPreExecute() {
                                 }

                                 @Override
                                 public void onPostExecute(Integer data) {
                                     Log.d(this.getClass(), "Sent" + data);
                                     if (data == 0) {
                                         Log.d(this.getClass(), "Sent like");
                                         new SentVkLike(mContext, Api.VKLIKE_GET + id);
                                     } else {
                                         Toast.makeText(mContext, "You already added Like this note", Toast.LENGTH_SHORT).show();
                                     }
                                 }

                                 @Override
                                 public void onError(Exception e) {
                                     onErrorSent(e);
                                 }
                             },
                Api.VKLIKEIS_GET + id,
                VkCachedDataSource.get(mContext),
                new LikeIsProcessor());
    }

    private void onErrorSent (Exception e){
        super.sentOnError(e);
    }

 }
