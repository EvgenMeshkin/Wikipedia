package by.evgen.android.apiclient.fragment;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.StorageCursorAdapter;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.db.StorageDBHelper;
import by.evgen.android.apiclient.db.provider.WikiContentProvider;
import by.evgen.android.apiclient.helper.GetAllVkStorage;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.FindResponder;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 05.02.2015.
 */
public abstract class AbstractDbFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, GetAllVkStorage.Callbacks {

    private View mContent;
    private SimpleCursorAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EditText mEditSearch;
    public ListView mListView;
    public String str;
    public String[] mFrom;
    public int[] mTo;
    public LoaderManager mLoadManager;
    public CursorLoader mCursorLoader;

    @Override
    public void onAllVkStorage(List<String> data) {

    }

    public interface Callbacks {
        void onShowDetails(NoteGsonModel note);
        void onErrorDialog(Exception e);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.text(getClass(), "Loader create");
        Context context = getActivity();
        String val = Constant.EMPTY;
        if (args != null) {
            val = args.getString(Constant.KEY);
            Log.text(getClass(), "Loader create" + val);
        }
        if (context != null) {
            //TODO make ? params
            mCursorLoader = getCursorLoader(val);
            Log.text(getClass(), mCursorLoader.toString());
            return mCursorLoader;
        } else {
            return  null;
        }
    }

    public abstract CursorLoader getCursorLoader(String val);

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.text(getClass(), "Loader finish" + data.toString());
        Context context = getActivity();
        if (context != null) {
            mAdapter = (SimpleCursorAdapter)getAdapter(context, data);
            mListView.setAdapter(mAdapter);
        }
    }

    public abstract SimpleCursorAdapter getAdapter(Context context, Cursor data);

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            mAdapter.swapCursor(null);
        }
    }

    void showDetails(NoteGsonModel note) {
        Callbacks callbacks = getCallbacks();
        callbacks.onShowDetails(note);
    }

    private Callbacks getCallbacks() {
        return FindResponder.findFirstResponder(this, Callbacks.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContent = inflater.inflate(R.layout.fragment_list_search, null);
        mEditSearch = (EditText) mContent.findViewById(R.id.editSearch);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mContent.findViewById(by.evgen.android.apiclient.R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        refreshData();
        return mContent;
    }

    private void refreshData(){
        sync();
        mLoadManager = getLoaderManager();
        mLoadManager.initLoader(1, null, this);
        mListView = (ListView) mContent.findViewById(android.R.id.list);
        mEditSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                str = s.toString();
                Log.text(getClass(), str);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY, str);
                refreshSearch(bundle);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) mListView.getAdapter().getItem(position);
                NoteGsonModel note = getNote(cursor);
                showDetails(note);
            }
        });
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public abstract Void sync();
    public abstract NoteGsonModel getNote(Cursor cursor);

    public void refreshSearch (Bundle bundle){
        Log.text(getClass(), "refresh");
        mLoadManager = getLoaderManager();
        mLoadManager.restartLoader(1, bundle, this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}

