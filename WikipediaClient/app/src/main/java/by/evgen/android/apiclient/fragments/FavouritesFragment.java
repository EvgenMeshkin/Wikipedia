package by.evgen.android.apiclient.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.FavouritesArrayAdapter;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.listener.FavouritesListViewListener;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.processing.StorageGetKeysProcessor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by User on 21.01.2015.
 */
public class FavouritesFragment extends AbstractFragment {

    private ArrayAdapter mAdapter;
    private ImageLoader mImageLoader;
    private Context mContext = getActivity();
    private ListView mListView;
    public static final int COUNT = 50;

    final static String LOG_TAG = FavouritesFragment.class.getSimpleName();

    @Override
    public View getViewLayout(LayoutInflater inflater) {
        View content = inflater.inflate(R.layout.fragment_list_search, null);
        mContext = getActivity();
        mListView = (ListView) content.findViewById(android.R.id.list);
        mImageLoader = ImageLoader.get(mContext);
        return content;
    }

    @Override
    public DataSource getDataSource() {
        return VkDataSource.get(mContext);
    }

    @Override
    public Processor getProcessor() {
        return new StorageGetKeysProcessor();
    }

    @Override
    public String getUrl() {
        return getUrl(COUNT, 0);
    }

    @Override
    public void onExecute(List data) {
        if (mAdapter == null) {
            mAdapter = new FavouritesArrayAdapter(mContext, R.layout.adapter_item, data);
            mListView.setFooterDividersEnabled(true);
            mListView.setAdapter(mAdapter);
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

    private String getUrl(int count, int offset) {
        String stor = Api.STORAGE_KEYS_GET + "&count=" + count + "&sroffset=" + offset;
        Log.d(LOG_TAG, "mKor=" + stor);
        return stor;
    }

}

