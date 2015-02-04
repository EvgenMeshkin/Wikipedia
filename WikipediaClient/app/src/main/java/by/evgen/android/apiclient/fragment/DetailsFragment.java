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
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.db.provider.WikiContentProvider;
import by.evgen.android.apiclient.helper.LikeVkNotes;
import by.evgen.android.apiclient.helper.SentsVkNotes;
import by.evgen.android.apiclient.helper.SentsVkStorage;
import by.evgen.android.apiclient.helper.WikiContentPageCallback;
import by.evgen.android.apiclient.processing.MobileViewProcessor;
import by.evgen.android.apiclient.source.DataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Decoder;
import by.evgen.android.apiclient.utils.FindResponder;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 22.10.2014.
 */


public class DetailsFragment extends AbstractFragment implements WikiContentPageCallback.Callbacks, SentsVkNotes.Callbacks {

    private MobileViewProcessor mMobileViewProcessor;
    private HttpDataSource mHttpDataSource;
    private NoteGsonModel mObj;
    private WebView mWebView;
    private List mContent;
    private View mProgress;
    private Context mContext;
    private String mTextHtml;
    private String mHistory;
    public ImageButton mImageButton;
    public ImageButton mNoteButton;
    public ImageButton mStorageButton;
    private final String WIKI_NAME = "name";
    private final String WIKI_DATE = "wikidate";

    @Override
    public void onStop() {
        dismissProgress();
        super.onStop();
    }

    @Override
    public void onReturnId(Long id) {

    }

    public interface Callbacks {
        void onSetContents(List data);
    }

    public void showDetails(NoteGsonModel note) {
        dismissProgress();
        super.showDetails(note);
    }

    public void setListData(List<Category> data) {
        if (data != null) {
            mContent = data;
            Callbacks callbacks = getCallbacks();
            callbacks.onSetContents(data);
        }
    }

    private Callbacks getCallbacks() {
        return FindResponder.findFirstResponder(this, Callbacks.class);
    }

    @Override
    public View getViewLayout(LayoutInflater inflater) {
        View content = inflater.inflate(R.layout.fragment_details, null);
        mContext = getActivity();
        mHttpDataSource = HttpDataSource.get(mContext);
        mObj = getArguments().getParcelable(Constant.KEY);
        if (mObj != null) {
            mHistory = mObj.getTitle();
            mWebView = (WebView) content.findViewById(R.id.webView);
            mMobileViewProcessor = new MobileViewProcessor(mContext, mHistory);
            mProgress = content.findViewById(android.R.id.progress);
            mImageButton = (ImageButton)content.findViewById(R.id.imageButton);
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new LikeVkNotes(mContext, mHistory.replaceAll(" ", "_"));
                }
            });
            mNoteButton = (ImageButton)content.findViewById(R.id.noteButton);
            mNoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.text(getClass(), "noteButton click");
                    new SentsVkNotes(DetailsFragment.this, mContext, mHistory.replaceAll(" ", "_"));
                }
            });
            mStorageButton = (ImageButton)content.findViewById(R.id.storageButton);
            mStorageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.text(getClass(), "sentStorage");
                    new SentsVkStorage(mContext, mHistory.replaceAll(" ", "_"));
                }
            });
            mWebView.setWebViewClient(new WikiWebViewClient());
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
        String url = Api.MOBILE_GET + Decoder.getHtml(mObj.getTitle());
        return url;
    }

    @Override
    public void onExecute(List data) {
        List<Category> mData = data;
        if (data == null || data.isEmpty()) {
            Toast.makeText(mContext, "Note added", Toast.LENGTH_SHORT).show();
        } else {
            mTextHtml = "";
//            ContentValues cv = new ContentValues();
//            cv.put(WIKI_NAME, mHistory);
//            cv.put(WIKI_DATE, new java.util.Date().getTime());
//            if (!cv.equals(null) && !mContext.equals(null)) {
//                mContext.getContentResolver().insert(WikiContentProvider.WIKI_HISTORY_URI, cv);
//            }
            new WikiContentPageCallback(mContext, this, Api.CONTENTS_GET + mHistory);
            for (int i = 0; i < data.size(); i++) {
                mTextHtml = mTextHtml + mData.get(i).getText();
            }
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            mWebView.setLongClickable(true);
            mWebView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.text(getClass(), "Long Click");
                    mImageButton.setVisibility(View.VISIBLE);
                    mNoteButton.setVisibility(View.VISIBLE);
                    mStorageButton.setVisibility(View.VISIBLE);
                    return false;
                }
            });
            mWebView.loadDataWithBaseURL(Api.MAIN_URL,
                    mTextHtml,
                    Constant.TYPE,
                    Constant.UTF,
                    null);
        }
    }

    public void notifyWebView(Integer position) {
        mWebView.loadDataWithBaseURL(Api.MAIN_URL + "#" + Decoder.getTitle(mContent.get(position).toString()),
                mTextHtml,
                Constant.TYPE,
                Constant.UTF,
                null);
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
            mHistory = Decoder.getUrlTitle(url);
            NoteGsonModel note = new NoteGsonModel(null, mHistory, mObj.getContent());
            showDetails(note);
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