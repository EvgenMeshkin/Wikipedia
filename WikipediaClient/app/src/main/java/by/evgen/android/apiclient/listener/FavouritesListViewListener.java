package by.evgen.android.apiclient.listener;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.processing.StorageGetKeysProcessor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by User on 21.01.2015.
 */
public class FavouritesListViewListener extends AbstractOnScrollListener {

    private Context mContext;

    public FavouritesListViewListener (Context context, ListView listView, ImageLoader imageLoader, List data, ArrayAdapter adapter, String value) {
    super.mImageLoader = imageLoader;
    super.mListView = listView;
    super.mData = data;
    super.mValue = value;
    super.mAdapter = adapter;
    mContext = context;
    super.mFooterProgress = View.inflate(context, R.layout.view_footer_progress, null);
}

    @Override
    public String getUrl(int count, int offset) {
        String url = Api.STORAGE_KEYS_GET + "&count="+count+"&sroffset="+offset;
        return url;
    }

    @Override
    public DataSource getDataSource() {
        return VkDataSource.get(mContext);
    }

    @Override
    public Processor getProcessor() {
        return new StorageGetKeysProcessor();
    }
}
