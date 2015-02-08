package by.evgen.android.apiclient.listener;

import android.content.Context;
import android.widget.ListView;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.processing.SearchPagesProcessor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.HttpDataSource;

/**
 * Created by User on 16.01.2015.
 */
public class SearchListViewOnScrollListener extends AbstractOnScrollListener {

    public SearchListViewOnScrollListener(Context context, ListView listView, List<Category> data, String value) {
        super(context, listView, data, value);
    }

    @Override
    public String getUrl(int count, int offset) {
        return Api.getSearchGet(count, offset) + mValue;
    }

    @Override
    public DataSource getDataSource() {
        return new HttpDataSource();
    }


    @Override
    public Processor getProcessor() {
        return new SearchPagesProcessor();
    }
}
