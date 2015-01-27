package by.evgen.android.apiclient.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.helper.WikiContentPageCallback;
import by.evgen.android.apiclient.processing.MobileViewProcessor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.utils.FindResponder;

/**
 * Created by User on 22.10.2014.
 */


public class DetailsFragment extends AbstractFragment implements WikiContentPageCallback.Callbacks {

    private MobileViewProcessor mMobileViewProcessor = new MobileViewProcessor();
    private HttpDataSource mHttpDataSource;
    private NoteGsonModel mObj;
    private WebView mWebView;
    private List mContent;
    private View mProgress;
    private Context mContext;
    private String mTextHtml;
    private String mHistory;
    private final Uri WIKI_URI = Uri
            .parse("content://by.evgen.android.apiclient.GeoData/geodata");
    private final String WIKI_NAME = "name";
    private final String WIKI_DATE = "wikidate";
    final String LOG_TAG = DetailsFragment.class.getSimpleName();

    @Override
    public void onStop() {
        dismissProgress();
        super.onStop();
    }

    public interface Callbacks {
        void onSetContents(List data);
    }

    public void showDetails(NoteGsonModel note) {
        dismissProgress();
        super.showDetails(note);
    }

    public void setListData(List<Category> data) {
        mContent = data;
        Callbacks callbacks = getCallbacks();
        callbacks.onSetContents(data);
    }

    private Callbacks getCallbacks() {
        return FindResponder.findFirstResponder(this, Callbacks.class);
    }

    @Override
    public View getViewLayout(LayoutInflater inflater) {
        View content = inflater.inflate(R.layout.fragment_details, null);
        mContext = getActivity();
        mHttpDataSource = HttpDataSource.get(mContext);
        mObj = getArguments().getParcelable("key");
        if (mObj != null) {
            mWebView = (WebView) content.findViewById(R.id.webView);
            mProgress = content.findViewById(android.R.id.progress);
            mWebView.setWebViewClient(new WikiWebViewClient());
            mHistory = mObj.getTitle().replaceAll(" ", "_");
            content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        }
        return content;
    }

    @Override
    public DataSource getDataSource() {
        return mHttpDataSource;
    }

    public MobileViewProcessor getProcessor() {
        return mMobileViewProcessor;
    }

    public String getUrl() {
        String url = Api.MOBILE_GET + Uri.encode(mObj.getTitle());
        return url;
    }

    @Override
    public void onExecute(List data) {
        List<Category> mData = data;
        if (data == null || data.isEmpty()) {
            Toast.makeText(mContext, "Note added", Toast.LENGTH_SHORT).show();
        } else {
            mTextHtml = "";
            ContentValues cv = new ContentValues();
            cv.put(WIKI_NAME, mHistory);
            cv.put(WIKI_DATE, new java.util.Date().getTime());
            if (!cv.equals(null) && !mContext.equals(null)) {
                mContext.getContentResolver().insert(WIKI_URI, cv);
            }
            new WikiContentPageCallback(mContext, this, Api.CONTENTS_GET + mHistory);
            for (int i = 0; i < data.size(); i++) {
                mTextHtml = mTextHtml + mData.get(i).getText();
            }
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBuiltInZoomControls(true);
            mWebView.loadDataWithBaseURL("https://en.wikipedia.org/", mTextHtml, "text/html", "utf-8", null);
        }
    }

    public void notifyWebView(Integer position) {
        mWebView.loadDataWithBaseURL("https://en.wikipedia.org/" + "#" + mContent.get(position).toString().replaceAll(" ", "_"), mTextHtml, "text/html", "utf-8", null);
        Log.i(LOG_TAG, mContent.get(position).toString().replaceAll(" ", "_"));
    }

    private void dismissProgress() {
        mProgress.setVisibility(View.GONE);
    }

    private void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }


    @Override
    public void onSetContents(List<Category> data) {
        setListData(data);
    }

    private class WikiWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Integer position = url.lastIndexOf("/");
            mHistory = url.substring(position + 1);
            NoteGsonModel note = new NoteGsonModel(null, mHistory, mObj.getContent());
            showDetails(note);
            Log.i(LOG_TAG, mHistory + mObj.getContent());
            showProgress();
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(mContext, description, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            dismissProgress();
        }
    }

}