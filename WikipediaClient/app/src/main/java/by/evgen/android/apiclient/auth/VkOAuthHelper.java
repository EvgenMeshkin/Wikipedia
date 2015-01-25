package by.evgen.android.apiclient.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import org.apache.http.auth.AuthenticationException;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.account.WikiAccount;
import by.evgen.android.apiclient.helper.WikiContentPageCallback;
import by.evgen.android.apiclient.utils.Log;


/**
 * Created by User on 30.10.2014.
 */
public class VkOAuthHelper {

    public static String mAccessToken;
    private static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String AUTORIZATION_URL = "https://oauth.vk.com/authorize?client_id=4613222&scope=offline,wall,photos,status,messages,notes&redirect_uri=" + REDIRECT_URL + "&display=mobile&response_type=token";
    public static final String ACCOUNT_TYPE = "by.evgen.android.apiclient";
    public static final String AUTHORITY = "by.evgen.android.apiclient";

    public static interface Callbacks {
        void onError(Exception e);
        void onSuccess();
    }

    public static String sign(String url) {
        if (url.contains("?")) {
            return url + "&access_token=" + mAccessToken;
        } else {
            return url + "?access_token=" + mAccessToken;
        }
    }

    public static boolean isLogged() {
        return !TextUtils.isEmpty(mAccessToken);
    }

    public static boolean proceedRedirectURL(Activity activity, String url, Callbacks callbacks) {
        Log.text(VkOAuthHelper.class, url);
        if (url.startsWith(REDIRECT_URL)) {
            Uri uri = Uri.parse(url);
            String fragment = uri.getFragment();
            Uri parsedFragment = Uri.parse("http://temp.com?" + fragment);
            String accessToken = parsedFragment.getQueryParameter("access_token");
            if (!TextUtils.isEmpty(accessToken)) {
                mAccessToken = accessToken;
                onTokenReceived(activity, callbacks, accessToken);
                return true;
            } else {
                String error = parsedFragment.getQueryParameter("error");
                String errorDescription = parsedFragment.getQueryParameter("error_description");
                String errorReason = parsedFragment.getQueryParameter("error_reason");
                if (!TextUtils.isEmpty(error)) {
                    callbacks.onError(new AuthenticationException(error + ", reason : " + errorReason + "(" + errorDescription + ")"));
                    return false;
                } else {
                    callbacks.onError(new AuthenticationException(error + ", unspecified error " + errorReason + "(" + errorDescription + ")"));
                }
            }
        }
        return false;
    }

    public static void onTokenReceived(Activity activity, Callbacks callbacks , String token) {
        AccountManager manager = AccountManager.get(activity);
        Account sAccount = new Account(activity.getString(R.string.acount_name), ACCOUNT_TYPE);
        if (manager.addAccountExplicitly(sAccount, activity.getPackageName(), new Bundle())) {
            ContentResolver.setSyncAutomatically(sAccount, AUTHORITY, true);
        }
        try {
            manager.setUserData(sAccount, "Token",  token);
            Log.text(activity.getClass(), "Saccount  -  " + manager.getUserData(sAccount, "Token"));

        } catch (Exception e) {
        }
        callbacks.onSuccess();
    }

}
