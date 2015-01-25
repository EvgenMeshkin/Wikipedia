package by.evgen.android.apiclient.helper;

import android.content.Context;
import android.graphics.Bitmap;


import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.auth.VkOAuthHelper;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.processing.BitmapProcessor;
import by.evgen.android.apiclient.processing.FotoIdUrlProcessor;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.apiclient.utils.Log;
import by.evgen.android.imageloader.CircleMaskedBitmap;

import java.util.List;

/**
 * Created by User on 20.12.2014.
 */
public class LoadVkUserData extends OnErrorCallbacks implements ManagerDownload.Callback<List<Category>> {

    private Callbacks mCallbacks;
    private Context mContext;

    public LoadVkUserData(Callbacks callbacks, Context context){
        super(context);
        mContext = context;
        ManagerDownload.load(this,
                Api.VKFOTOS_GET,
                VkDataSource.get(context),
                new FotoIdUrlProcessor());
        mCallbacks = callbacks;
    }

    public static interface Callbacks {
        void onUserData(Bitmap foto, String first, String last);
    }

   @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(List<Category> data) {
        Category item = data.get(0);
        String url = item.getUrlFoto();
        final String first = item.getFirstName();
        final String last = item.getLastName();
        Log.text(this.getClass(), "Load url " + url);
        ManagerDownload.load(new ManagerDownload.Callback<Bitmap>() {

                                 @Override
                                 public void onPreExecute() {

                                 }

                                 @Override
                                 public void onPostExecute(Bitmap bitmap) {
                                     bitmap = CircleMaskedBitmap.getCircleMaskedBitmapUsingShader(bitmap, 100);
                                     mCallbacks.onUserData(bitmap, first, last);
                                 }

                                 @Override
                                 public void onError(Exception e) {
                                     onErrorSent(e);
                                 }
                             },
                url,
                HttpDataSource.get(mContext),
                new BitmapProcessor());
    }

   @Override
    public void onError(Exception e) {
        onErrorSent(e);
    }

    private void onErrorSent (Exception e){
        super.sentOnError(e);
    }

}
