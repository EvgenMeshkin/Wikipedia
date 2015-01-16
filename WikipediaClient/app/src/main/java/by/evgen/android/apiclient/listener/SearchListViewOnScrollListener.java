package by.evgen.android.apiclient.listener;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.processing.SearchPagesProcessor;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by User on 16.01.2015.
 */
public class SearchListViewOnScrollListener implements AbsListView.OnScrollListener, ManagerDownload.Callback<List<Category>> {
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    private ListView mListView;
    private List<Category> mData;
    private ArrayAdapter mAdapter;
    private View mFooterProgress;
    private boolean isImageLoaderControlledByDataManager = false;
    public static final int COUNT = 50;
    private ImageLoader mImageLoader;
    private boolean isPagingEnabled = true;
    private String mValue;

    public SearchListViewOnScrollListener(Context context, ListView listView, ImageLoader imageLoader, List data, ArrayAdapter adapter, String value) {
        mImageLoader = imageLoader;
        mListView = listView;
        mData = data;
        mValue = value;
        mAdapter = adapter;
        mFooterProgress = View.inflate(context, R.layout.view_footer_progress, null);
    }

    private String getUrl(int count, int offset) {
        String url = Api.SEARCH_GET + "srlimit="+count+"&sroffset="+offset + "&srsearch=" + mValue;
        return url;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                if (!isImageLoaderControlledByDataManager) {
                    mImageLoader.resume();
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                if (!isImageLoaderControlledByDataManager) {
                    mImageLoader.pause();
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                if (!isImageLoaderControlledByDataManager) {
                    mImageLoader.pause();
                }
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        ListAdapter adapter = view.getAdapter();
        final int count = getRealAdapterCount(adapter);
        if (count == 0) {
            return;
        }
        if (previousTotal != totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            previousTotal = totalItemCount;
            isImageLoaderControlledByDataManager = true;
            ManagerDownload.load(this, getUrl(COUNT, count), new VkDataSource(),new SearchPagesProcessor());
        }
        if (mData != null && mData.size() == COUNT) {
            isPagingEnabled = true;
        } else {
            isPagingEnabled = false;
        }
        refreshFooter();
    }

    private void updateAdapter(List<Category> data) {
        if (data != null && data.size() == COUNT) {
            isPagingEnabled = true;
            mListView.addFooterView(mFooterProgress, null, false);
        } else {
            isPagingEnabled = false;
            mListView.removeFooterView(mFooterProgress);
        }
        if (data != null) {
            mData.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
    }


    public static int getRealAdapterCount(ListAdapter adapter) {
        if (adapter == null) {
            return 0;
        }
        int count = adapter.getCount();
        if (adapter instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
            count = count - headerViewListAdapter.getFootersCount() - headerViewListAdapter.getHeadersCount();
        }
        return count;
    }

    private void refreshFooter() {
        if (mFooterProgress != null) {
            if (isPagingEnabled) {
                mFooterProgress.setVisibility(View.VISIBLE);
            } else {
                mFooterProgress.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPreExecute() {
        mImageLoader.pause();
    }

    @Override
    public void onPostExecute(List<Category> data) {
        updateAdapter(data);
        refreshFooter();
        mImageLoader.resume();
        isImageLoaderControlledByDataManager = false;
    }

    @Override
    public void onError(Exception e) {
        onError(e);
        mImageLoader.resume();
        isImageLoaderControlledByDataManager = false;
    }
}
