package by.evgen.android.apiclient;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.fragments.AbstractFragment;
import by.evgen.android.apiclient.fragments.SearchFragment;

/**
 * Created by evgen on 20.01.2015.
 */
public class SearchFragmentActivity extends ActionBarActivity implements AbstractFragment.Callbacks {

    private NoteGsonModel mNoteGsonModel;// = (NoteGsonModel) getIntent().<Bundle>getParcelableExtra("key");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//setDisplayShowTitleEnabled(true);
        String search = getIntent().getStringExtra(SearchManager.QUERY);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SearchFragment fragmentmain = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", search);
        fragmentmain.setArguments(bundle);
        transaction.replace(R.id.framemain, fragmentmain);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.wikimain, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
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
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.search:
                onSearchRequested();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onShowDetails(NoteGsonModel note) {
        mNoteGsonModel = (NoteGsonModel) note;
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

    }

}

