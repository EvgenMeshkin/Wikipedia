package by.evgen.android.apiclient.account;

import android.accounts.Account;
import android.os.Parcel;

/**
 * Created by evgen on 23.01.2015.
 */
public class WikiAccount extends Account {

    public static final String TYPE = "by.evgen.android.apiclient";

    public static final String TOKEN_FULL_ACCESS = "by.evgen.android.apiclient.TOKEN_FULL_ACCESS";

    public static final String KEY_PASSWORD = "com.github.elegion.KEY_PASSWORD";

    public WikiAccount(Parcel in) {
        super(in);
    }

    public WikiAccount(String name) {
        super(name, TYPE);
    }

}