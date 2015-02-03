package by.evgen.android.apiclient.activity;

/**
 * Created by User on 30.10.2014.
 */

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
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
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.db.provider.WikiContentProvider;
import by.evgen.android.apiclient.dialogs.ErrorDialog;
import by.evgen.android.apiclient.fragment.AbstractFragment;
import by.evgen.android.apiclient.fragment.FavouritesFragment;
import by.evgen.android.apiclient.fragment.MainPageFragment;
import by.evgen.android.apiclient.fragment.RandomCategoryFragment;
import by.evgen.android.apiclient.fragment.StorageFragment;
import by.evgen.android.apiclient.fragment.WatchListFragment;
import by.evgen.android.apiclient.fragment.WikiFragment;
import by.evgen.android.apiclient.helper.ClearVkStorage;
import by.evgen.android.apiclient.helper.LoadVkUserData;
import by.evgen.android.apiclient.helper.OnErrorCallbacks;
import by.evgen.android.apiclient.helper.RandomPageCallback;
import by.evgen.android.apiclient.utils.EnumMenuItems;
import by.evgen.android.apiclient.utils.Log;

public class WikiActivity extends ActionBarActivity implements AbstractFragment.Callbacks<NoteGsonModel>, LoadVkUserData.Callbacks, RandomPageCallback.Callbacks, WatchListFragment.Callbacks, OnErrorCallbacks.Callbacks {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private View mHeaderDrawer;
    private final String KEY = "key";
    private final String KEYNOTE = "keynote";
    private MenuItem mClearHist;
    private boolean mVisible = false;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        mTitle = getTitle();
        if (Authorized.isLogged()){
            Log.text(getClass(), "LoadDataUser  -  " );
            new LoadVkUserData(this, this);
            mHeaderDrawer = View.inflate(this, R.layout.view_header, null);
        } else {
            mHeaderDrawer = View.inflate(this, R.layout.view_header_start, null);
        }
        mDrawerTitle = getResources().getString(R.string.menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.addHeaderView(mHeaderDrawer);
        mDrawerList.setHeaderDividersEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//setDisplayShowTitleEnabled(true);
        displayView(1);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar,
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                mDrawerList.setAdapter(null);
                supportInvalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                mDrawerList.setAdapter(new MenuAdapter(WikiActivity.this, EnumMenuItems.values()));
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
        Log.text(this.getClass(), "FirstName" + first);
        TextView firstname = (TextView) mHeaderDrawer.findViewById(R.id.text1);
        TextView lastname = (TextView) mHeaderDrawer.findViewById(R.id.text2);
        ImageView fotos = (ImageView) mHeaderDrawer.findViewById(R.id.icon);
        fotos.setImageBitmap(foto);
        firstname.setText(first);
        lastname.setText(last);
    }

    @Override
    public void onShowDetails(NoteGsonModel note) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY, note);
        Intent intent = new Intent();
        intent.setClass(this, DetailsFragmentActivity.class);
        intent.putExtra(KEY, bundle);
        intent.putExtra(KEYNOTE, note);
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
      if (position <= EnumMenuItems.values().length) {
      String name = getResources().getString(EnumMenuItems.values()[position-1].getTitle());
      Log.text(getClass(), name);
          FragmentTransaction transactionWiki = getSupportFragmentManager().beginTransaction();
         // transactionWiki.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);
          switch (EnumMenuItems.values()[position-1].valueOf(name)) {
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
                  WikiFragment fragmentWiki = new WikiFragment();
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
         // transactionWiki.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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
        MenuItem search =  menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mClearHist =  menu.findItem(R.id.clearHist);
        mClearHist.setVisible(mVisible);
        return true;
    }

    public void clearHist (MenuItem item) {
        for(Fragment frag : getSupportFragmentManager().getFragments()) {
            if (frag instanceof WatchListFragment) {
                Log.text(getClass(), "Clear history");
                this.getContentResolver().delete(WikiContentProvider.WIKI_HISTORY_URI, null, null);
            }
            if (frag instanceof FavouritesFragment) {
                Log.text(getClass(), "Clear Storage");
                new ClearVkStorage(this);
            }
        }
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