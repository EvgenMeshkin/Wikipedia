package by.evgen.android.apiclient.utils;

import android.app.Activity;
import android.content.Context;


/**
 * Created by User on 14.01.2015.
 */
public final class Log {

    private static Boolean mOff = true;

    public static synchronized void text(Class name, String text) {
        if (!mOff) {
            android.util.Log.d(name.getSimpleName(), text);
        }
    }

}
