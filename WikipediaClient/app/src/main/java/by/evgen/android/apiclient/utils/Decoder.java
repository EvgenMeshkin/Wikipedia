package by.evgen.android.apiclient.utils;

import android.net.Uri;

/**
 * Created by evgen on 29.01.2015.
 */
public class Decoder {

    public static String getTitle(String str){
        return str.replaceAll(" ", "_");
    }

    public static String getHtml(String str){
        return Uri.encode(str);
    }

    public static String getUrlTitle(String str){
        Integer position = str.lastIndexOf("/");
        return str.substring(position + 1);
    }

}
