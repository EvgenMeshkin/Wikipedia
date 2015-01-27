package by.evgen.android.apiclient.helper;

import android.content.Context;

import by.evgen.android.apiclient.processing.StringProcessor;
import by.evgen.android.apiclient.source.VkDataSource;

/**
 * Created by evgen on 13.01.2015.
 */
public class SentVkLike {

    public SentVkLike (final Context context, final String url){
       ManagerDownload.load(new ManagerDownload.Callback() {
           @Override
           public void onPreExecute() {

           }

           @Override
           public void onPostExecute(Object data) {

           }

           @Override
           public void onError(Exception e) {

           }
       }, url, VkDataSource.get(context), new StringProcessor());

    }
}
