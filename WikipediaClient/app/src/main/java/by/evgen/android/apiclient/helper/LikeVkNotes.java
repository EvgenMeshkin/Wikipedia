package by.evgen.android.apiclient.helper;

import android.content.Context;
import android.widget.Toast;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.auth.VkOAuthHelper;
import by.evgen.android.apiclient.processing.LikeIsProcessor;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 12.01.2015.
 */
public class LikeVkNotes implements SentsVkNotes.Callbacks{

    private Context mContext;

    public LikeVkNotes (Context context, String url){
        mContext = context;
        new SentsVkNotes(this, context, url);

    }

    @Override
    public void onReturnId(final Long id) {
        ManagerDownload.load(new ManagerDownload.Callback<String>() {
                                 @Override
                                 public void onPreExecute() {
                                 }

                                 @Override
                                 public void onPostExecute(String data) {
                                     Log.text(this.getClass(), "Sent" + data);
                                     if (data.equals("0")) {
                                         Log.text(this.getClass(), "Sent like");
                                         new SentVkLike(mContext, Api.VKLIKE_GET + id);
                                     } else {
                                         Toast.makeText(mContext, "You already added Like this note", Toast.LENGTH_SHORT).show();
                                     }
                                 }

                                 @Override
                                 public void onError(Exception e) {
                                     onError(e);
                                 }
                             },
                Api.VKLIKEIS_GET + id,
                new HttpDataSource(),
                new LikeIsProcessor());
    }
}
