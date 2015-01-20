package by.evgen.android.apiclient;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.fragments.AbstractFragment;
import by.evgen.android.apiclient.fragments.DetailsFragment;
import by.evgen.android.apiclient.helper.LikeVkNotes;
import by.evgen.android.apiclient.helper.SentsVkNotes;
import by.evgen.android.apiclient.listener.RightDrawerItemClickListener;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 13.11.2014.
 */
public class DetailsFragmentActivity extends ActionBarActivity implements AbstractFragment.Callbacks, SentsVkNotes.Callbacks, DetailsFragment.Callbacks {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView mDrawerListRight;
    private ActionBarDrawerToggle mDrawerToggle;
    private NoteGsonModel mNoteGsonModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//setDisplayShowTitleEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark_material_light));
        mDrawerListRight = (ListView) findViewById(R.id.list_right_menu);

        Log.text(getClass(),"activity");// obj.getId() + mHistory + obj.getContent() );

        DetailsFragment details = new DetailsFragment();
        mDrawerListRight.setOnItemClickListener(details);//new RightDrawerItemClickListener());

        details.setArguments(getIntent().<Bundle>getParcelableExtra("key"));
            getSupportFragmentManager().beginTransaction().add(
                    R.id.framemain, details).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search");
    //    searchView.setOnQueryTextListener(this);
        return true;
    }

    public void sentNote(MenuItem item) {
        Log.text(getClass(), "sentNote");
        if (!mNoteGsonModel.equals(null)) {
            new SentsVkNotes(this, this, mNoteGsonModel.getTitle().replaceAll(" ", "_"));
        }
    }

    public void sentLike(MenuItem item) {
        Log.text(getClass(), "sentLike");
        if (!mNoteGsonModel.equals(null)) {
            new LikeVkNotes(this, mNoteGsonModel.getTitle().replaceAll(" ", "_"));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onShowDetails(NoteGsonModel note) {
//        NoteGsonModel noteGsonModel = (NoteGsonModel) note;
        Bundle bundle = new Bundle();
        bundle.putParcelable("key", note);
        Intent intent = new Intent();
        intent.setClass(this, DetailsFragmentActivity.class);
        intent.putExtra("key", bundle);
        startActivity(intent);
    }

    @Override
    public void onErrorDialog(Exception e) {

    }

    @Override
    public void onReturnId(Long id) {

    }

    @Override
    public void onSetContents(List data) {
        mDrawerListRight.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,android.R.id.text2, data));
    }
}
