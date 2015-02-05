package by.evgen.android.apiclient.source;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import org.apache.http.auth.AuthenticationException;

import java.io.InputStream;

import by.evgen.android.apiclient.auth.Authorized;
import by.evgen.android.apiclient.WikiApplication;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.auth.secure.EncrManager;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 15.11.2014.
 */
public class VkCachedDataSource extends CachedHttpDataSource {

    public static final String KEY = "VkCachedDataSource";
    private Context mContext;

    public VkCachedDataSource(Context context) {
        super(context);
        mContext = context;
    }

    public static VkCachedDataSource get(Context context) {
        return WikiApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        if (Authorized.isLogged()) {
            AccountManager manager = AccountManager.get(mContext);
            Account vkAccount = new Account(mContext.getString(R.string.acount_name), Constant.ACCOUNT_TYPE);
            String url = p + "&access_token=" + EncrManager.decrypt(mContext, manager.getUserData(vkAccount, "Token"));
            Log.text(mContext.getClass(), "Datasoaccount  -  " + manager.getUserData(vkAccount, "Token"));
            return super.getResult(url);
        } else {
            throw new AuthenticationException("You must login");
        }
   }

}