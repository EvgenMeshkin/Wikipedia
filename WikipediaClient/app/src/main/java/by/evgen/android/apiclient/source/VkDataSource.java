package by.evgen.android.apiclient.source;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.widget.Toast;

import java.io.InputStream;

import by.evgen.android.apiclient.Authorized;
import by.evgen.android.apiclient.CoreApplication;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.auth.secure.EncrManager;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 15.11.2014.
 */
public class VkDataSource extends CachedHttpDataSource {

    public static final String KEY = "VkDataSource";
    public static final String ACCOUNT_TYPE = "by.evgen.android.apiclient";
    private Context mContext;

    public VkDataSource(Context context) {
        super(context);
        mContext = context;
    }

    public static VkDataSource get(Context context) {
        if (!Authorized.isLogged()) {
            Toast.makeText(context, "You must login", Toast.LENGTH_SHORT).show();
            return null;
        }
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        if (Authorized.isLogged()) {
            AccountManager manager = AccountManager.get(mContext);
            Account vkAccount = new Account(mContext.getString(R.string.acount_name), ACCOUNT_TYPE);
            String url = p + "&access_token=" + EncrManager.decrypt(mContext, manager.getUserData(vkAccount, "Token"));
            Log.text(mContext.getClass(), "Datasoaccount  -  " + manager.getUserData(vkAccount, "Token"));
            return super.getResult(url);
        } else {
            Toast.makeText(mContext, "You must login", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}