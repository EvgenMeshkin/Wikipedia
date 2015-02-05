package by.evgen.android.apiclient.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
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
import by.evgen.android.apiclient.db.provider.WikiContentProvider;
import by.evgen.android.apiclient.processing.MobileViewProcessor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Decoder;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 24.01.2015.
 */
public class MainPageFragment extends AbstractFragment {

    private MobileViewProcessor mMobileProcessor;
    private HttpDataSource mHttpDataSource;
    private WebView mWebView;
    private View mProgress;
    private Context mContext;

   @Override
    public void onStop() {
        dismissProgress();
        super.onStop();
    }

    public void showDetails(NoteGsonModel note) {
        dismissProgress();
        super.showDetails(note);
    }

    @Override
    public View getViewLayout(LayoutInflater inflater) {
        View content = inflater.inflate(R.layout.fragment_details, null);
        mContext = getActivity();
        mMobileProcessor = new MobileViewProcessor(mContext, "Main Page");
        mHttpDataSource = HttpDataSource.get(mContext);
        mWebView = (WebView) content.findViewById(R.id.webView);
        mProgress = content.findViewById(android.R.id.progress);
        mWebView.setWebViewClient(new WikiWebViewClient());
        content.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        return content;
    }

    @Override
    public DataSource getDataSource() {
        return mHttpDataSource;
    }

    public MobileViewProcessor getProcessor() {
        return mMobileProcessor;
    }

    public String getUrl() {
        String url = Api.MOBILE_GET + Decoder.getHtml("Main page");
        return url;
    }

    @Override
    public void onExecute(List data) {
        List<Category> mData = data;
        String mTextHtml = Constant.EMPTY;
        if (data == null || data.isEmpty()) {
            Toast.makeText(mContext, "No data", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < data.size(); i++) {
                mTextHtml = mTextHtml + mData.get(i).getText();
            }
            Log.text(getClass(), mTextHtml);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.loadDataWithBaseURL(Api.MAIN_URL,
                    mTextHtml,
                    Constant.TYPE,
                    Constant.UTF,
                    null);
        }
    }

    private void dismissProgress() {
        mProgress.setVisibility(View.GONE);
    }

    private void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private class WikiWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String mHistory = Decoder.getUrlTitle(url);
            NoteGsonModel note = new NoteGsonModel(null, mHistory, " ");
            showDetails(note);
            showProgress();
            return true;
        }

        public void onReceivedError(WebView view, int    errorCode, String description, String failingUrl) {
            Toast.makeText(mContext, description, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            dismissProgress();
        }
    }

}
