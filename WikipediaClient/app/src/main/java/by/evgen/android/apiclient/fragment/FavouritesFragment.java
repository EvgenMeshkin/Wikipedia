package by.evgen.android.apiclient.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.FavouritesArrayAdapter;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.helper.ClearVkStorage;
import by.evgen.android.apiclient.listener.FavouritesListViewListener;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.processing.StorageGetKeysProcessor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.VkCachedDataSource;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.apiclient.utils.Log;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by User on 21.01.2015.
 */
public class FavouritesFragment extends AbstractFragment {

    private ArrayAdapter mAdapter;
    private ImageLoader mImageLoader;
    private Context mContext = getActivity();
    private ListView mListView;
    private EditText mEditSearch;
    public List<String> mData;
    public static final int COUNT = 50;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View getViewLayout(LayoutInflater inflater) {
        View content = inflater.inflate(R.layout.fragment_list_search, null);
        mContext = getActivity();
        mListView = (ListView) content.findViewById(android.R.id.list);
        mImageLoader = ImageLoader.get(mContext);
        mEditSearch = (EditText) content.findViewById(R.id.editText);
        mSwipeRefreshLayout = (SwipeRefreshLayout) content.findViewById(by.evgen.android.apiclient.R.id.swipe_container);
        return content;
    }

    @Override
    public DataSource getDataSource() {
        return VkDataSource.get(mContext);
    }

    @Override
    public Processor getProcessor() {
        return new StorageGetKeysProcessor(mContext);
    }

    @Override
    public String getUrl() {
        return getUrl(COUNT, 0);
    }

    @Override
    public void onExecute(final List data) {
        mData = data;
        if (mAdapter == null) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshAdapter(data);
                }
            });
            mAdapter = new FavouritesArrayAdapter(mContext, R.layout.adapter_item, data);
            mListView.setFooterDividersEnabled(true);
            mListView.setAdapter(mAdapter);
            mEditSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String str = s.toString();
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).contains(str)) {
                            list.add(mData.get(i));
                        }
                    }
                    refreshAdapter(list);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mListView.setOnScrollListener(new FavouritesListViewListener(getActivity(), mListView, mImageLoader, data, mAdapter, null)); //{
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final String item = (String) mAdapter.getItem(position);
                    NoteGsonModel note = new NoteGsonModel(null, item, item);
                    showDetails(note);
                }
            });
        } else {
            data.clear();
        }
    }

    public void refreshAdapter(List<String> list) {
        Context context = getActivity();
        if (context != null) {
            Log.text(getClass(), "refresh adapter");
            mAdapter = new FavouritesArrayAdapter(mContext, R.layout.adapter_item, list);
            mListView.setAdapter(mAdapter);
        }
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private String getUrl(int count, int offset) {
        String stor = Api.getStorageKeysGet(count, offset);
        return stor;
    }

    public void clearStorage(){
        new ClearVkStorage(mContext);
    }

}

