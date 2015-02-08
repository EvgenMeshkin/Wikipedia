package by.evgen.android.apiclient.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.listener.SearchListViewOnScrollListener;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.processing.SearchPagesProcessor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.utils.Constant;

/**
 * Created by User on 18.12.2014.
 */

public class SearchFragment extends AbstractFragment<NoteGsonModel> {

    private String mValue;
    private ListView mListView;
    private DataSource mVkDataSource;
    private SearchPagesProcessor mSearchPagesProcessor = new SearchPagesProcessor();

    @Override
    public View getViewLayout(LayoutInflater inflater) {
        View content = inflater.inflate(R.layout.fragment_wiki, null);
        Context context = getActivity();
        mVkDataSource = HttpDataSource.get(context);
        mListView = (ListView) content.findViewById(android.R.id.list);
        mValue = getArguments().getString(Constant.KEY);
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
        int count = 20;
        int offset = 0;
        return getUrl(count, offset);
    }

    @Override
    public void onExecute(final List<Category> data) {
            mListView.setOnScrollListener(new SearchListViewOnScrollListener(getActivity(), mListView, data,  mValue));
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Category item = data.get(position);
                    NoteGsonModel note = new NoteGsonModel(item.getId(), item.getTitle(), item.getNs());
                    showDetails(note);
                }
            });
    }

    private String getUrl(int count, int offset) {
        return Api.getSearchGet(count, offset) + mValue;
    }

}
