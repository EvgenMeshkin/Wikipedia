package by.evgen.android.apiclient.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import by.evgen.android.apiclient.activity.VkLoginActivity;

/**
 * Created by evgen on 22.01.2015.
 */
public class WikiAuthenticator extends AbstractAccountAuthenticator {

    private final Context mContext;

    public WikiAuthenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
//        final Intent intent = new Intent(mContext, VkLoginActivity.class);
//        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
//        final Bundle bundle = new Bundle();
//        if (options != null) {
//            bundle.putAll(options);
//        }
//        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
//        final Bundle result = new Bundle();
//        final AccountManager am = AccountManager.get(mContext.getApplicationContext());
//        String authToken = am.peekAuthToken(account, authTokenType);
//        if (!TextUtils.isEmpty(authToken)) {
//            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
//            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
//            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
//        } else {
//            final Intent intent = new Intent(mContext, VkLoginActivity.class);
//            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
//            final Bundle bundle = new Bundle();
//            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
//        }
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
