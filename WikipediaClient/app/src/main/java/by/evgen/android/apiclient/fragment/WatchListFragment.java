package by.evgen.android.apiclient.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.DateAdapter;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.FindResponder;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 06.01.2015.
 */
public class WatchListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private View mContent;
    private DateAdapter mAdapter;
    public ListView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EditText mEditSearch;
    public  String str;
    public String[] mFrom;
    public int[] mTo;
    public LoaderManager mLoadermanager;
    public CursorLoader mCursorLoader;
    final Uri WIKI_URI = Uri
            .parse("content://by.evgen.android.apiclient.GeoData/geodata");
    private final String KEY = "key";

    public interface Callbacks {
        void onShowDetails(NoteGsonModel note);
        void onErrorDialog(Exception e);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.text(getClass(), "Loader create");
        Context context = getActivity();
        String val = Constant.getEmpty();
        if (args != null) {
            val = args.getString(KEY);
            Log.text(getClass(), "Loader create" + val);
        }
        if (context != null) {
            mCursorLoader = new CursorLoader(getActivity(), WIKI_URI, mFrom, "name LIKE  '%" + val + "%'", null, null);
            Log.text(getClass(), mCursorLoader.toString());
            return mCursorLoader;
        } else {
            return  null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.text(getClass(), "Loader finish" + data.toString());
        Context context = getActivity();
        if (context != null) {
            mFrom = new String[]{Constant.getDbid(), Constant.getDbName(), Constant.getDbDate()};
            mTo = new int[]{R.id.text1, R.id.text2};
            mAdapter = new DateAdapter(context, R.layout.adapter_item, data, mFrom, mTo);
            mListView.setAdapter(mAdapter);
        }
    }

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
        mEditSearch = (EditText) mContent.findViewById(R.id.editText);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mContent.findViewById(by.evgen.android.apiclient.R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData();
            }
        });
        setData();
        return mContent;
    }

    private void setData (){
        mLoadermanager = getLoaderManager();
        mLoadermanager.initLoader(1, null, this);
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
                bundle.putString(KEY, str);
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
                NoteGsonModel note = new NoteGsonModel(cursor.getLong(cursor.getColumnIndex(Constant.getDbid())),
                        cursor.getString(cursor.getColumnIndex(Constant.getDbName())),
                        cursor.getString(cursor.getColumnIndex(Constant.getDbDate())));
                showDetails(note);
            }
        });
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void refreshSearch (Bundle bundle){
        Log.text(getClass(), "refresz");
        mLoadermanager = getLoaderManager();
        mLoadermanager.initLoader(2, bundle, this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
