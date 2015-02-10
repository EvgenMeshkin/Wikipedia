package by.evgen.android.apiclient.activity;

/**
 * Created by User on 30.10.2014.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.MenuAdapter;
import by.evgen.android.apiclient.auth.Authorized;
import by.evgen.android.apiclient.auth.VkOAuthHelper;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.db.WikiContentProvider;
import by.evgen.android.apiclient.dialogs.ErrorDialog;
import by.evgen.android.apiclient.fragment.AbstractDbFragment;
import by.evgen.android.apiclient.fragment.AbstractFragment;
import by.evgen.android.apiclient.fragment.MainPageFragment;
import by.evgen.android.apiclient.fragment.NearbyFragment;
import by.evgen.android.apiclient.fragment.RandomCategoryFragment;
import by.evgen.android.apiclient.fragment.StorageFragment;
import by.evgen.android.apiclient.fragment.WatchListFragment;
import by.evgen.android.apiclient.helper.OnErrorCallbacks;
import by.evgen.android.apiclient.helper.vkhelper.ClearVkStorage;
import by.evgen.android.apiclient.helper.vkhelper.LoadVkUserData;
import by.evgen.android.apiclient.helper.wikihelper.RandomPageCallback;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.EnumMenuItems;
import by.evgen.android.apiclient.utils.Log;

public class WikiActivity extends ActionBarActivity implements AbstractFragment.Callbacks<NoteGsonModel>, LoadVkUserData.Callbacks, RandomPageCallback.Callbacks, AbstractDbFragment.Callbacks, OnErrorCallbacks.Callbacks {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private View mHeaderDrawer;
    private boolean mVisible = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        mTitle = getTitle();
        Account[] accounts = null;
        AccountManager accountManager = AccountManager.get(this);
        if (accountManager != null) {
            accounts = accountManager.getAccountsByType(VkOAuthHelper.ACCOUNT_TYPE);
        }
        assert accounts != null;
        for (Account account : accounts) {
            String userId = accountManager.getUserData(account, "Token");
            if (userId != null) {
                Authorized.setLogged(true);
                break;
            }
        }
        if (Authorized.isLogged()) {
            Log.d(getClass(), "LoadDataUser  -  ");
            new LoadVkUserData(this, this);
            mHeaderDrawer = View.inflate(this, R.layout.view_header, null);
        } else {
            mHeaderDrawer = View.inflate(this, R.layout.view_header_start, null);
        }
        mDrawerTitle = getResources().getString(R.string.menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.addHeaderView(mHeaderDrawer);
        mDrawerList.setHeaderDividersEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        displayView(1);
        final MenuAdapter menuAdapter = new MenuAdapter(WikiActivity.this, EnumMenuItems.values());
        mDrawerList.setAdapter(menuAdapter);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                menuAdapter.notifyDataSetChanged();
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    public void onUserData(Bitmap foto, String first, String last) {
        Log.d(this.getClass(), "FirstName" + first);
        TextView firstName = (TextView) mHeaderDrawer.findViewById(R.id.title);
        TextView lastName = (TextView) mHeaderDrawer.findViewById(R.id.content);
        ImageView fotos = (ImageView) mHeaderDrawer.findViewById(R.id.icon);
        fotos.setImageBitmap(foto);
        firstName.setText(first);
        lastName.setText(last);
    }

    @Override
    public void onShowDetails(NoteGsonModel note) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.KEY, note);
        Intent intent = new Intent();
        intent.setClass(this, DetailsFragmentActivity.class);
        intent.putExtra(Constant.KEY, bundle);
        intent.putExtra(Constant.KEYN, note);
        startActivity(intent);
    }

    @Override
    public void onErrorDialog(Exception e) {
        DialogFragment newFragment = ErrorDialog.newInstance(e.getMessage());
        newFragment.show(getSupportFragmentManager(), Constant.DIALOG);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(
                AdapterView<?> parent, View view, int position, long id
        ) {
            displayView(position);
        }
    }

    private void displayView(int position) {
        if (position <= EnumMenuItems.values().length) {
            String name = getResources().getString(EnumMenuItems.values()[position - 1].getTitle());
            Log.d(getClass(), name);
            FragmentTransaction transactionWiki = getSupportFragmentManager().beginTransaction();
            switch (EnumMenuItems.valueOf(name)) {
                case Home:
                    MainPageFragment fragmentPage = new MainPageFragment();
                    transactionWiki.replace(R.id.framemain, fragmentPage);
                    mVisible = false;
                    mDrawerLayout.closeDrawer(mDrawerList);
                    break;
                case Random:
                    RandomCategoryFragment categoryFragment = new RandomCategoryFragment();
                    transactionWiki.replace(R.id.framemain, categoryFragment);
                    mVisible = false;
                    mDrawerLayout.closeDrawer(mDrawerList);
                    break;
                case Nearby:
                    NearbyFragment fragmentWiki = new NearbyFragment();
                    transactionWiki.replace(R.id.framemain, fragmentWiki);
                    mVisible = false;
                    break;
                case Favourites:
                    StorageFragment fragmentFavor = new StorageFragment();
                    transactionWiki.replace(R.id.framemain, fragmentFavor);
                    mVisible = true;
                    break;
                case Watchlist:
                    WatchListFragment fragmentWatch = new WatchListFragment();
                    transactionWiki.replace(R.id.framemain, fragmentWatch);
                    mVisible = true;
                    break;
                case Log_in:
                    startActivity(new Intent(this, StartActivity.class));
                default:
                    mDrawerLayout.closeDrawer(mDrawerList);
                    return;
            }
            transactionWiki.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transactionWiki.commit();
            mTitle = name;
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.wikimain, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        MenuItem clearHist = menu.findItem(R.id.clearHist);
        clearHist.setVisible(mVisible);
        return true;
    }

    public void clearHist(MenuItem item) {
        for (Fragment frag : getSupportFragmentManager().getFragments()) {
            if (frag instanceof WatchListFragment) {
                Log.d(getClass(), "Clear history");
                this.getContentResolver().delete(WikiContentProvider.WIKI_HISTORY_URI, null, null);
            }
            if (frag instanceof StorageFragment) {
                Log.d(getClass(), "Clear Storage");
                new ClearVkStorage(this);
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.setClass(this, SearchFragmentActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.search:
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}