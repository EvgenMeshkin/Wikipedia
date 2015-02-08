package by.evgen.android.apiclient.utils;

import by.evgen.android.apiclient.R;

/**
 * Created by User on 02.02.2015.
 */
public enum EnumMenuItems {

    Home(R.string.home, R.drawable.home),
    Random(R.string.random, R.drawable.random),
    Nearby(R.string.nearby, R.drawable.nearby),
    Favourites(R.string.favourites, R.drawable.storage),
    Watchlist(R.string.watchlist, R.drawable.watchlist),
    Log_in(R.string.log_in, R.drawable.log_in);

    private int mTitle;
    private int mIcon;

    EnumMenuItems(int title, int image) {
        mTitle = title;
        mIcon = image;
    }

    public int getTitle() {
        return mTitle;
    }

    public int getIcon() {
        return mIcon;
    }
}