package by.evgen.android.apiclient.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.dialogs.ErrorDialog;
import by.evgen.android.apiclient.fragment.AbstractFragment;
import by.evgen.android.apiclient.fragment.DetailsFragment;
import by.evgen.android.apiclient.helper.OnErrorCallbacks;
import by.evgen.android.apiclient.helper.vkhelper.LikeVkNotes;
import by.evgen.android.apiclient.helper.vkhelper.SentsVkNotes;
import by.evgen.android.apiclient.helper.vkhelper.SentsVkStorage;
import by.evgen.android.apiclient.helper.wikihelper.WikiGetIdTitle;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Decoder;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 13.11.2014.
 */
public class DetailsFragmentActivity extends ActionBarActivity implements AbstractFragment.Callbacks<NoteGsonModel>, DetailsFragment.Callbacks, OnErrorCallbacks.Callbacks, WikiGetIdTitle.Callbacks {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListRight;
    private NoteGsonModel mNoteGsonModel;
    private final int stub_id = R.drawable.right_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListRight = (ListView) findViewById(R.id.list_right_menu);
        if (getIntent().getParcelableExtra(Constant.KEYN) != null) {
            mNoteGsonModel = getIntent().getParcelableExtra(Constant.KEYN);
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
        details.setArguments(getIntent().<Bundle>getParcelableExtra(Constant.KEY));
        getSupportFragmentManager().beginTransaction().add(
                R.id.framemain, details).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        MenuItem like = menu.findItem(R.id.action_like);
        like.setTitle(setColorItem(getString(R.string.like)));
        MenuItem note = menu.findItem(R.id.action_note);
        note.setTitle(setColorItem(getString(R.string.add_note)));
        MenuItem storage = menu.findItem(R.id.saving_pages);
        storage.setTitle(setColorItem(getString(R.string.saving_pages)));
        return true;
    }

    private CharSequence setColorItem (String title) {
        SpannableStringBuilder text = new SpannableStringBuilder();
        text.append(title);
        text.setSpan(new ForegroundColorSpan(Color.BLUE),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.setClass(this, SearchFragmentActivity.class);
            kkkhkjl
                    kjj
            startActivity(intent);
        }
    }

    @Override
    public void onSetIdTitle(List<Category> data) {
        Category category = data.get(0);
        String pageId = category.getPageId();
        new SentsVkStorage(this, pageId, Decoder.getHtml(mNoteGsonModel.getTitle()));
        Log.d(getClass(), "pageId = " + pageId);
    }

    public void sentStorage(MenuItem item) {
        Log.d(getClass(), "sentStorage");
        new WikiGetIdTitle(this, this, "&titles=" + Decoder.getHtml(mNoteGsonModel.getTitle()));
    }


    public void sentNote(MenuItem item) {
        Log.d(getClass(), "sentNote");
        new SentsVkNotes(null, this, Decoder.getTitle(mNoteGsonModel.getTitle()));
    }

    public void sentLike(MenuItem item) {
        Log.d(getClass(), "sentLike");
        new LikeVkNotes(this, Decoder.getTitle(mNoteGsonModel.getTitle()));
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
        bundle.putParcelable(Constant.KEY, note);
        Intent intent = new Intent();
        intent.setClass(this, DetailsFragmentActivity.class);
        intent.putExtra(Constant.KEY, bundle);
        intent.putExtra(Constant.KEYN, note);
        startActivity(intent);
    }

    @Override
    public void onErrorDialog(Exception e) {
        e.printStackTrace();
        Log.d(getClass(), "OnError  " + e);
        DialogFragment newFragment = ErrorDialog.newInstance(e.getMessage());
        newFragment.show(getSupportFragmentManager(), Constant.DIALOG);
    }

    @Override
    public void onSetContents(List<Category> data) {
        if (data != null && mDrawerListRight.getAdapter() == null) {
            mDrawerListRight.setAdapter(new ArrayAdapter<Category>(this, R.layout.drawer_list_item, android.R.id.content, data) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(DetailsFragmentActivity.this, R.layout.drawer_list_item, null);
                    }
                    ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
                    imageView.setImageResource(stub_id);
                    TextView textView = (TextView) convertView.findViewById(android.R.id.content);
                    textView.setText(getItem(position).getLine());
                    if (position == 0) {
                        textView.setText(mNoteGsonModel.getTitle());
                    }
                    TranslateAnimation anim = new TranslateAnimation(150, 0, 0, 0);
                    anim.setDuration(1000 + position * 100);
                    convertView.startAnimation(anim);
                    convertView.setTag(position);
                    return convertView;
                }
            });
        }
    }

}
