package by.evgen.android.apiclient.fragment;


import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.adapters.RecyclerWikiAdapter;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.listener.RecyclerItemClickListener;
import by.evgen.android.apiclient.processing.CategoryArrayProcessor;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.service.GpsLocation;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 30.10.2014.
 */
public class NearbyFragment extends AbstractFragment<NoteGsonModel> implements GpsLocation.Callbacks {

    private RecyclerWikiAdapter mAdapter;
    private List<Category> mData;
    private static String mLocation = "53.677226|23.8489383";
    private Context mContext;
    private CategoryArrayProcessor mCategoryArrayProcessor = new CategoryArrayProcessor();
    private RecyclerView mRecyclerView;

    @Override
    public void onShowKor(String latitude) {
        mLocation = latitude;
        Log.d(getClass(), "latitude=" + mLocation);
        super.load(getUrl(), getDataSource(), getProcessor());
    }

    @Override
    public View getViewLayout(LayoutInflater inflater) {
        View content = inflater.inflate(by.evgen.android.apiclient.R.layout.fragment_wiki_recycler, null);
        mRecyclerView = (RecyclerView) content.findViewById(by.evgen.android.apiclient.R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        mContext = getActivity();
        GpsLocation gpsLocation = new GpsLocation();
        gpsLocation.getLocation(this, getActivity());
        return content;
    }

    @Override
    public DataSource getDataSource() {
        return HttpDataSource.get(getActivity());
    }

    @Override
    public Processor getProcessor() {
        return mCategoryArrayProcessor;
    }

    @Override
    public String getUrl() {
        Log.d(getClass(), "mLocation=" + mLocation);
        return Api.GEOSEARCH_GET + mLocation;
    }

    public void showDetails(NoteGsonModel note){
        super.showDetails(note);
    }

    @Override
    public void onExecute(List<Category> data) {
        if (mAdapter == null) {
            mAdapter = new RecyclerWikiAdapter(mContext, data);
            mRecyclerView.setAdapter(mAdapter);
            mData = data;
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                           Category item = mData.get(position);
                           NoteGsonModel note = new NoteGsonModel(item.getId(), item.getTitle(), item.getNs());
                           showDetails(note);
                        }
                    })
            );
        } else {
            mData.clear();
            mData.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
    }

}
