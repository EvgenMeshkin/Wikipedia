package by.evgen.android.apiclient.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.RandomArrayAdapter;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.processing.CategoryMembersProcessor;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.utils.Log;
import by.evgen.android.imageloader.CircleMaskedBitmap;

/**
 * Created by User on 28.01.2015.
 */
public class RandomCategoryFragment extends AbstractFragment {

    private ArrayAdapter mAdapter;
    private Context mContext;
    private ListView mListView;
    private DataSource mHttpDataSource;
    private final int COUNT = 50;
    private CategoryMembersProcessor mCategoryProcessor = new CategoryMembersProcessor();

    @Override
    public View getViewLayout(LayoutInflater inflater) {
        View content = inflater.inflate(R.layout.fragment_wiki, null);
        mContext = getActivity();
        mHttpDataSource = HttpDataSource.get(mContext);
        mListView = (ListView) content.findViewById(android.R.id.list);
        return content;
    }

    @Override
    public DataSource getDataSource() {
        return mHttpDataSource;
    }

    @Override
    public Processor getProcessor() {
        return mCategoryProcessor;
    }

    @Override
    public String getUrl() {
        return getUrl(COUNT, 0);
    }

    @Override
    public void onExecute(List data) {
        Log.text(getClass(), data.toString());
        if (mAdapter == null) {
            CircleMaskedBitmap.CIRCLE = false;
            mAdapter = new RandomArrayAdapter(mContext, R.layout.adapter_item_cardview, data);
            mListView.setFooterDividersEnabled(true);
            mListView.setAdapter(mAdapter);
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
        String search = Api.RANDOM_PAGE_GET;
        return search;
    }

    @Override
    public void onStop() {
        CircleMaskedBitmap.CIRCLE = true;
        super.onStop();
    }
}

