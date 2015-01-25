package by.evgen.android.apiclient.helper;

import android.content.Context;

/**
 * Created by evgen on 24.01.2015.
 */
public class OnErrorCallbacks {

    private Context mContext;
    private Callbacks iCallbacks;

    public OnErrorCallbacks(Context context) {
        mContext = context;
    }

    public interface Callbacks {
        void onErrorDialog(Exception e);
    }

    public void sentOnError (Exception e){
        iCallbacks = (Callbacks) mContext;
        iCallbacks.onErrorDialog(e);
    }

}
