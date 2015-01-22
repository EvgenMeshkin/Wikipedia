package by.evgen.android.apiclient.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.SearchArrayAdapter;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.listener.SearchListViewOnScrollListener;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.processing.SearchPagesProcessor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.imageloader.ImageLoader;

import java.util.List;

/**
 * Created by User on 18.12.2014.
 */

public class SearchFragment extends AbstractFragment {

    private ArrayAdapter mAdapter;
    private ImageLoader mImageLoader;
    private Context mContext;
    private static String mKor;
    private String mValue;
    private ListView mListView;
    private VkDataSource mVkDataSource;
    public static final int COUNT = 50;
    private View footerProgress;
    final static String LOG_TAG = SearchFragment.class.getSimpleName();
    private SearchPagesProcessor mSearchPagesProcessor = new SearchPagesProcessor();

    @Override
    public View getViewLayout(LayoutInflater inflater) {
        View content = inflater.inflate(R.layout.fragment_wiki, null);
        mContext = getActivity();
        mVkDataSource = VkDataSource.get(mContext);
        mListView = (ListView) content.findViewById(android.R.id.list);
        mValue = getArguments().getString("key");
        mImageLoader = ImageLoader.get(mContext);
        return content;
    }

    @Override
    public DataSource getDataSource() {
        return mVkDataSource;
    }

    @Override
    public Processor getProcessor() {
        return mSearchPagesProcessor;
    }

    @Override
    public String getUrl() {
        return getUrl(COUNT, 0);
    }

    @Override
    public void onExecute(List data) {
        footerProgress = View.inflate(mContext, R.layout.view_footer_progress, null);
        if (mAdapter == null) {
            mAdapter = new SearchArrayAdapter(mContext, R.layout.adapter_item, data);
            mListView.setFooterDividersEnabled(true);
            mListView.addFooterView(footerProgress, null, false);
            mListView.setAdapter(mAdapter);
            mListView.setOnScrollListener(new SearchListViewOnScrollListener(getActivity(), mListView, mImageLoader, data, mAdapter, mValue)); //{
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Category item = (Category) mAdapter.getItem(position);
                    NoteGsonModel note = new NoteGsonModel(item.getId(), item.getTitle(), item.getNs());
                    showDetails(note);
                }
            });
        } else {
            data.clear();
        }
    }

    private String getUrl(int count, int offset) {
        mKor = Api.SEARCH_GET + "srlimit="+count+"&sroffset="+offset + "&srsearch=" + mValue;
        Log.d(LOG_TAG, "mKor="+mKor);
        return mKor;
    }

}
