package by.evgen.android.apiclient.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.dialogs.ErrorDialog;
import by.evgen.android.apiclient.fragment.AbstractFragment;
import by.evgen.android.apiclient.fragment.SearchFragment;
import by.evgen.android.apiclient.utils.Constant;

/**
 * Created by evgen on 20.01.2015.
 */
public class SearchFragmentActivity extends ActionBarActivity implements AbstractFragment.Callbacks<NoteGsonModel> {

    public NoteGsonModel mNoteGsonModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        String search = getIntent().getStringExtra(SearchManager.QUERY);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SearchFragment fragmentMain = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY, search);
        fragmentMain.setArguments(bundle);
        transaction.replace(R.id.framemain, fragmentMain);
        transaction.commit();
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
        clearHist.setVisible(false);
        return true;
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
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.search:
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onShowDetails(NoteGsonModel note) {
        mNoteGsonModel = note;
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.KEY, note);
        Intent intent = new Intent();
        intent.setClass(this, DetailsFragmentActivity.class);
        intent.putExtra(Constant.KEY, bundle);
        intent.putExtra(Constant.KEYN, note);
        startActivity(intent);
    }

    public void clearHist(MenuItem item) {

    }

    @Override
    public void onErrorDialog(Exception e) {
        DialogFragment newFragment = ErrorDialog.newInstance(e.getMessage());
        newFragment.show(getSupportFragmentManager(), Constant.DIALOG);
    }

}

