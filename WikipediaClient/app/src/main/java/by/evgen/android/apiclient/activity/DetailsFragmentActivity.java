package by.evgen.android.apiclient.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.dialogs.ErrorDialog;
import by.evgen.android.apiclient.fragment.AbstractFragment;
import by.evgen.android.apiclient.fragment.DetailsFragment;
import by.evgen.android.apiclient.helper.LikeVkNotes;
import by.evgen.android.apiclient.helper.OnErrorCallbacks;
import by.evgen.android.apiclient.helper.SentsVkNotes;
import by.evgen.android.apiclient.helper.SentsVkStorage;
import by.evgen.android.apiclient.utils.Decoder;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 13.11.2014.
 */
public class DetailsFragmentActivity extends ActionBarActivity implements AbstractFragment.Callbacks<NoteGsonModel>, SentsVkNotes.Callbacks, DetailsFragment.Callbacks, OnErrorCallbacks.Callbacks {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListRight;
    private NoteGsonModel mNoteGsonModel;
    private final String KEY = "key";
    private final String KEYNOTE = "keynote";
    private final int stub_id = R.drawable.right_drawer;

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
        if (getIntent().getParcelableExtra(KEYNOTE) != null) {
        mNoteGsonModel =  getIntent().getParcelableExtra(KEYNOTE);
        }
        DetailsFragment details = new DetailsFragment();
        mDrawerListRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailsFragment fragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.framemain);
                fragment.notifyWebView(position);
                mDrawerLayout.closeDrawer(mDrawerListRight);
            }
        });
        details.setArguments(getIntent().<Bundle>getParcelableExtra(KEY));
            getSupportFragmentManager().beginTransaction().add(
                    R.id.framemain, details).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
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

    public void sentStorage (MenuItem item) {
        Log.text(getClass(), "sentStorage");
        new SentsVkStorage(this, Decoder.getTitle(mNoteGsonModel.getTitle()));
    }


    public void sentNote(MenuItem item) {
        Log.text(getClass(), "sentNote");
        new SentsVkNotes(this, this, Decoder.getTitle(mNoteGsonModel.getTitle()));
    }

    public void sentLike(MenuItem item) {
        Log.text(getClass(), "sentLike");
              new LikeVkNotes(this, Decoder.getTitle(mNoteGsonModel.getTitle()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings:
                return true;
            case R.id.search:
                onSearchRequested();
                return true;
            case R.id.action_note:
                return true;
            case R.id.action_like:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onShowDetails(NoteGsonModel note) {
        mNoteGsonModel = note;
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
        e.printStackTrace();
        Log.text(getClass(), "OnError  " + e);
        DialogFragment newFragment = ErrorDialog.newInstance(e.getMessage());
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onReturnId(Long id) {

    }

    @Override
    public void onSetContents(List data) {
        if (data != null) {
            mDrawerListRight.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, android.R.id.text2, data) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(DetailsFragmentActivity.this, R.layout.drawer_list_item, null);
                    }
                    ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
                    imageView.setImageResource(stub_id);
                    TextView textView = (TextView) convertView.findViewById(android.R.id.text2);
                    textView.setText(getItem(position));
                    if (position == 0){
                        textView.setText(mNoteGsonModel.getTitle());
                    }
                    convertView.setTag(position);
                    return convertView;
                }
            });
        }
    }

}
