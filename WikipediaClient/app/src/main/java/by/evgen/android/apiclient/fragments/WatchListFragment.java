package by.evgen.android.apiclient.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.DateAdapter;
import by.evgen.android.apiclient.adapters.FavouritesArrayAdapter;
import by.evgen.android.apiclient.adapters.SearchArrayAdapter;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.utils.FindResponder;
import by.evgen.android.apiclient.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evgen on 06.01.2015.
 */
public class WatchListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private View mContent;
    private TextView mEmpty;
    private DateAdapter mAdapter;
    private Cursor mCursor;
    private Cursor mCursorSearch;
    public ListView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EditText mEditSearch;
    public  String str = "";
    public String[] mFrom;
    public int[] mTo;
    final Uri WIKI_URI = Uri
            .parse("content://by.evgen.android.apiclient.GeoData/geodata");

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface Callbacks {
        void onShowDetails(NoteGsonModel note);
        void onErrorDialog(Exception e);
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
        mEmpty = (TextView) mContent.findViewById(android.R.id.empty);
        mEmpty.setVisibility(View.GONE);
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
        mCursor = getActivity().getContentResolver().query(WIKI_URI, null, null,
                null, null);
//        mCursor.moveToFirst();
//        final List<String> list = new ArrayList<String>();
//        if (mCursor.moveToFirst()) {
//            do {
//                list.add(mCursor.getString(mCursor.getColumnIndex("name")));
//            } while (mCursor.moveToNext());
//        }
//        mCursor.moveToFirst();
//        final List<Long> listData = new ArrayList<Long>();
//        if (mCursor.moveToFirst()) {
//            do {
//                listData.add(mCursor.getLong(mCursor.getColumnIndex("wikidate")));
//
//            } while (mCursor.moveToNext());
//        }


        mListView = (ListView) mContent.findViewById(android.R.id.list);
        mFrom = new String[]{"name", "wikidate"};
        mTo = new int[]{R.id.text1, R.id.text2};
        mCursor.moveToFirst();
        if (mAdapter == null) {
            mAdapter = new DateAdapter(getActivity(), R.layout.adapter_item, mCursor, mFrom, mTo);
        }
        mListView.setAdapter(mAdapter);
        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                str = s.toString();
                Log.text(getClass(), str);
                if (mCursor.moveToFirst()) {
                final List<String> list = new ArrayList<String>();
            do {
                String name = mCursor.getString(mCursor.getColumnIndex("name"));
                if (name.contains(str)) {
                    list.add(name);
//                    Log.text(getClass(), mCursorSearch.getString(mCursorSearch.getColumnIndex("name")));
//                    mAdapter = new DateAdapter(getActivity(), R.layout.adapter_item, mCursorSearch, mFrom, mTo);
//                    //mAdapter.notifyDataSetChanged();
//                    mListView.setAdapter(mAdapter);
                }
            } while (mCursor.moveToNext());
                    mListView.setAdapter(new FavouritesArrayAdapter(getActivity(), R.layout.adapter_item, list));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) mAdapter.getItem(position);
                NoteGsonModel note = new NoteGsonModel(cursor.getLong(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("wikidate")));
                showDetails(note);
            }
        });
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onStop() {
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        super.onStop();
    }

}
