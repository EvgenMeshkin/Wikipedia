package by.evgen.android.apiclient.fragments;



import android.app.ProgressDialog;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.helper.WikiContentPageCallback;
import by.evgen.android.apiclient.processing.ContentsArrayProcessor;
import by.evgen.android.apiclient.processing.MobileViewProcessor;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.apiclient.utils.FindResponder;


import java.util.List;

/**
 * Created by User on 22.10.2014.
 */

//TODO check with Default WebViewFragment
public class DetailsFragment extends AbstractFragment implements WikiContentPageCallback.Callbacks, ListView.OnItemClickListener {

    private View content;
    private MobileViewProcessor mMobileViewProcessor = new MobileViewProcessor();
    private NoteGsonModel obj;
    //TODO remove static
    private static WebView mWebView;
    private static List<Category> mData;
    private static List mContent;
    //TODO remove static
    private static String mTextHtml;
    private String mHistory;
    private final Uri WIKI_URI = Uri
            .parse("content://com.example.evgenmeshkin.GeoData/geodata");
    private final String WIKI_NAME = "name";
    private final String WIKI_DATE = "wikidate";
    final String LOG_TAG = DetailsFragment.class.getSimpleName();

    public interface Callbacks {
        void onSetContents(List data);
    }

    public void showDetails(NoteGsonModel note){
        super.showDetails(note);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setListener(position);
    }

    public  void setListData(List data) {
        mContent = data;
        Callbacks callbacks = getCallbacks();
        callbacks.onSetContents(data);
    }

    private Callbacks getCallbacks() {
        return FindResponder.findFirstResponder(this, Callbacks.class);
    }

    @Override
    public View getViewLayout(LayoutInflater inflater) {
        content = inflater.inflate(R.layout.fragment_details, null);
        //TODO create variable
        if (getArguments() != null) {
            obj = (NoteGsonModel) getArguments().getParcelable("key");
        }
        mWebView = (WebView) content.findViewById(R.id.webView);
        mWebView.setWebViewClient(new WikiWebViewClient());
        mHistory = obj.getTitle().replaceAll(" ", "_");
        content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
//        //TODO use URLEncoder, URLDecoder
        String url = Api.MOBILE_GET + obj.getTitle().replaceAll(" ", "%20");
        return   content;
    }

    @Override
    public DataSource getDataSource() {
        return VkDataSource.get(getActivity());
    }

    public MobileViewProcessor getProcessor() {
        return mMobileViewProcessor;
    }

    public String getUrl() {
        String url = Api.MOBILE_GET + obj.getTitle().replaceAll(" ", "%20");
        return url;
    }

    @Override
    public void onExecute(List data) {
        mData = data;
        mTextHtml = "";
        ContentValues cv = new ContentValues();
        cv.put(WIKI_NAME, mHistory);
        cv.put(WIKI_DATE, new java.util.Date().getTime());
        //getActivity().getContentResolver().delete(WIKI_URI, null, null);
        if (!cv.equals(null)) {
            getActivity().getContentResolver().insert(WIKI_URI, cv);
        }
        new WikiContentPageCallback(this, Api.CONTENTS_GET + mHistory);
        if (data == null || data.isEmpty()) {
            //TODO empty!!! this is not error!!!
            //this is not error state
            onError(new NullPointerException("No data"));
        } else {
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

    public  void setListener(Integer position) {
        mWebView.loadDataWithBaseURL("https://en.wikipedia.org/" + "#" + mContent.get(position).toString().replaceAll(" ", "_"), mTextHtml, "text/html", "utf-8", null);
        Log.i(LOG_TAG, mContent.get(position).toString().replaceAll(" ", "_")  );
    }

    @Override
    public void onSetContents(List data) {
        setListData(data);
    }

    //TODO create new activity
    private class WikiWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Integer position = url.lastIndexOf("/");
            mHistory = url.substring(position + 1);
            NoteGsonModel note = new NoteGsonModel(null, mHistory, obj.getContent());
            showDetails(note);
            Log.i(LOG_TAG, mHistory + obj.getContent() );
            content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            content.findViewById(android.R.id.progress).setVisibility(View.GONE);
         }
    }

}