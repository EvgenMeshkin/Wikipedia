package by.evgen.android.apiclient.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.utils.FindResponder;

/**
 * Created by User on 15.01.2015.
 */
public abstract class AbstractFragment <T> extends Fragment implements ManagerDownload.Callback<List<Category>> {

    private DataSource mDatasource;
    private Processor mProcessor;
    private String mUrl;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mProgress;

    public interface Callbacks<T> {
        void onShowDetails(T note);
        void onErrorDialog(Exception e);
    }

    private Callbacks getCallbacks() {
        return FindResponder.findFirstResponder(this, Callbacks.class);
    }

    public void showDetails(T note) {
        mProgress.setVisibility(View.GONE);
        Callbacks callbacks = getCallbacks();
        callbacks.onShowDetails(note);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View content = getViewLayout(inflater);
        mDatasource = getDataSource();
        mProcessor = getProcessor();
        mUrl = getUrl();
        mProgress = content.findViewById(android.R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) content.findViewById(by.evgen.android.apiclient.R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load(mUrl, mDatasource, mProcessor);
            }
        });
        load(mUrl, mDatasource, mProcessor);
        return content;
    }

    public abstract View getViewLayout(LayoutInflater inflater);

    public abstract DataSource getDataSource();

    public abstract Processor getProcessor();

    public abstract String getUrl();

    public abstract void onExecute(List<Category> data);

    public void load(String Url, DataSource dataSource, Processor processor) {
        ManagerDownload.load(this,
                Url,
                dataSource,
                processor);
    }

    @Override
    public void onPreExecute() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPostExecute(List<Category> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mProgress.setVisibility(View.GONE);
        if (data != null) {
            onExecute(data);
        }
    }

    @Override
    public void onError(Exception e) {
        mProgress.setVisibility(View.GONE);
        e.printStackTrace();
        Callbacks callbacks = getCallbacks();
        callbacks.onErrorDialog(e);
    }

}
