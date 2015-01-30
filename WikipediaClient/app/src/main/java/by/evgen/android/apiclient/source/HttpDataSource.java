package by.evgen.android.apiclient.source;

import android.content.Context;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import by.evgen.android.apiclient.WikiApplication;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 18.10.2014.
 */
public class HttpDataSource implements DataSource<InputStream, String> {

    public static final String KEY = "HttpDataSource";

    public static HttpDataSource get(Context context) {
        Log.text(HttpDataSource.class, "getContext");
        return WikiApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        URL url = new URL(p);
        return url.openStream();
    }

    public static void close(Closeable in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

