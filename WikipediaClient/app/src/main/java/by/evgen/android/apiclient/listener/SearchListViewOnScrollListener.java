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
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.processing.SearchPagesProcessor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by User on 16.01.2015.
 */
public class SearchListViewOnScrollListener extends AbstractOnScrollListener {
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    private ListView mListView;
    private List<Category> mData;
    private ArrayAdapter mAdapter;
    private View mFooterProgress;
    private boolean isImageLoaderControlledByDataManager = false;
    public static final int COUNT = 4;
    private ImageLoader mImageLoader;
    private boolean isPagingEnabled = true;
    private String mValue;

    public SearchListViewOnScrollListener(Context context, ListView listView, ImageLoader imageLoader, List data, ArrayAdapter adapter, String value) {
        super.mImageLoader = imageLoader;
        super.mListView = listView;
        super.mData = data;
        super.mValue = value;
        super.mAdapter = adapter;
        super.mFooterProgress = View.inflate(context, R.layout.view_footer_progress, null);
    }


    @Override
    public String getUrl(int count, int offset) {
             String url = Api.SEARCH_GET + "srlimit="+count+"&sroffset="+offset + "&srsearch=" + mValue;
            return url;

    }

    @Override
    public DataSource getDataSource() {
        return new VkDataSource();
    }

    @Override
    public Processor getProcessor() {
        return new SearchPagesProcessor();
    }
}
