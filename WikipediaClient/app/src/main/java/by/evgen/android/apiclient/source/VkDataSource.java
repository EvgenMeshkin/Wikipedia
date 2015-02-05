package by.evgen.android.apiclient.source;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import org.apache.http.auth.AuthenticationException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.WikiApplication;
import by.evgen.android.apiclient.auth.Authorized;
import by.evgen.android.apiclient.auth.secure.EncrManager;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 03.02.2015.
 */
public class VkDataSource implements DataSource<InputStream, String> {

    public static final String KEY = "VkDataSource";
    private Context mContext;


    public VkDataSource(Context context) {
        mContext = context;
    }

    public static VkDataSource get(Context context) {
        Log.text(VkDataSource.class, "getContext");
        return WikiApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
      if (Authorized.isLogged()) {
            AccountManager manager = AccountManager.get(mContext);
            Account vkAccount = new Account(mContext.getString(R.string.acount_name), Constant.ACCOUNT_TYPE);
            URL url = new URL(p + "&access_token=" + EncrManager.decrypt(mContext, manager.getUserData(vkAccount, "Token")));
            Log.text(mContext.getClass(), "Datasoaccount  -  " + manager.getUserData(vkAccount, "Token"));
            return url.openStream();
        } else {
            throw new AuthenticationException("You must login");
        }
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
