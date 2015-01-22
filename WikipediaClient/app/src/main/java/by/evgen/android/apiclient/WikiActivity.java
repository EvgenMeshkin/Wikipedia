package by.evgen.android.apiclient;

/**
 * Created by User on 30.10.2014.
 */

import android.accounts.Account;
import android.accounts.AccountManager;


import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.content.ContentResolver;
import android.os.Build;
import android.os.Bundle;

import android.content.res.Configuration;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.*;
import by.evgen.android.apiclient.auth.VkOAuthHelper;
import by.evgen.android.apiclient.auth.secure.EncrManager;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.dialogs.ErrorDialog;
import by.evgen.android.apiclient.fragments.AbstractFragment;
import by.evgen.android.apiclient.fragments.FavouritesFragment;
import by.evgen.android.apiclient.fragments.SearchFragment;
import by.evgen.android.apiclient.fragments.WatchListFragment;
import by.evgen.android.apiclient.fragments.WikiFragment;
import by.evgen.android.apiclient.helper.RandomPageCallback;
import by.evgen.android.apiclient.helper.LoadVkUserData;
import by.evgen.android.apiclient.utils.Log;

//TODO clear unused code
public class WikiActivity extends ActionBarActivity implements AbstractFragment.Callbacks<NoteGsonModel>, LoadVkUserData.Callbacks, RandomPageCallback.Callbacks, WatchListFragment.Callbacks {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // navigation drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    private String[] viewsNames;
    public static final String ACCOUNT_TYPE = "by.evgen.android.apiclient.account";
    public static final String AUTHORITY = "by.evgen.android.apiclient";
    public static final int requestL = 0;
    private AccountManager mAm;
    //TODO why static?
    public static Account sAccount;
    private View  mDetailsFrame;
    private View headerDrawer;
    private enum mMenuValue {Home, Random, Nearby, Favourites, Watchlist, Settings, Log_in};

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        mTitle = getTitle();
        mDrawerTitle = getResources().getString(R.string.menu);
        viewsNames = getResources().getStringArray(R.array.views_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
        headerDrawer = View.inflate(this, R.layout.view_header, null);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.addHeaderView(headerDrawer);
        mDrawerList.setHeaderDividersEnabled(true);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, android.R.id.text2, viewsNames));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//setDisplayShowTitleEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar,
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
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
        mAm = AccountManager.get(this);
//        LoadRandomPage load = new LoadRandomPage();
//        load.loadingRandomPage(this);
        if (sAccount == null) {
            sAccount = new Account(getString(R.string.news), ACCOUNT_TYPE);
        }
        if (mAm.addAccountExplicitly(sAccount, getPackageName(), new Bundle())) {
            ContentResolver.setSyncAutomatically(sAccount, AUTHORITY, true);
        }
        try {
//            mDrawerList.addHeaderView(headerDrawer);
            mAm.setUserData(sAccount, "Token", EncrManager.encrypt(this, VkOAuthHelper.mAccessToken));
            LoadVkUserData loadVkUserData = new LoadVkUserData(this);
        } catch (Exception e) {
        }
    }

    @Override
    public void onUserData(Bitmap foto, String first, String last) {
        Log.text(this.getClass(), "FirstName" + first);
        TextView firstname = (TextView) headerDrawer.findViewById(R.id.text1);
        TextView lastname = (TextView) headerDrawer.findViewById(R.id.text2);
        ImageView fotos = (ImageView) headerDrawer.findViewById(R.id.icon);
        fotos.setImageBitmap(foto);
        firstname.setText(first);
        lastname.setText(last);
    }

    @Override
    public void onShowDetails(NoteGsonModel note) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("key", note);
        Intent intent = new Intent();
        intent.setClass(this, DetailsFragmentActivity.class);
        intent.putExtra("key", bundle);
        intent.putExtra("keynote", note);
        startActivity(intent);
    }

    @Override
    public void onErrorDialog(Exception e) {
        DialogFragment newFragment = ErrorDialog.newInstance(e.getMessage());
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(
                AdapterView<?> parent, View view, int position,  long id
        ) {
            displayView(position);
        }
    }

    private void displayView(int position) {
      if (position != 0) {
          switch (mMenuValue.valueOf(viewsNames[position - 1])) {
              case Home:
                  FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                  SearchFragment fragmentmain = new SearchFragment();
                  transaction.replace(R.id.framemain, fragmentmain);
                  transaction.commit();
                  mDrawerLayout.closeDrawer(mDrawerList);
                  break;
              case Random:
                  RandomPageCallback load = new RandomPageCallback();
                  load.loadingRandomPage(this);
                  mDrawerLayout.closeDrawer(mDrawerList);
                  break;
              case Nearby:
                  FragmentTransaction transactionwiki = getSupportFragmentManager().beginTransaction();
                  WikiFragment fragmentwiki = new WikiFragment();
                  transactionwiki.replace(R.id.framemain, fragmentwiki);
                  transactionwiki.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                  transactionwiki.commit();
                  mDrawerLayout.closeDrawer(mDrawerList);
                  break;
              case Favourites:
                  FragmentTransaction transactionFavorit = getSupportFragmentManager().beginTransaction();
                  FavouritesFragment fragmentFavor = new FavouritesFragment();
                  transactionFavorit.replace(R.id.framemain, fragmentFavor);
                  transactionFavorit.commit();
                  mDrawerLayout.closeDrawer(mDrawerList);
                  break;
              case Watchlist:
                  FragmentTransaction transactionwatch = getSupportFragmentManager().beginTransaction();
                  WatchListFragment fragmentwatch = new WatchListFragment();
                  transactionwatch.replace(R.id.framemain, fragmentwatch);
                  transactionwatch.commit();
                  mDrawerLayout.closeDrawer(mDrawerList);
                  break;
              case Settings:

                  break;

              case Log_in:
//                  FragmentTransaction buck = getSupportFragmentManager().beginTransaction();
//                  SearchFragment fragmentbuck = new SearchFragment();
//                  buck.replace(R.id.framemain, fragmentbuck);
//                  buck.commit();
                  startActivity(new Intent(this, StartActivity.class));
              default:
                  mDrawerLayout.closeDrawer(mDrawerList);
                  break;
          }
      }
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.wikimain, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem search =  menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
       if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
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
            case R.id.action_settings:
                return true;
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