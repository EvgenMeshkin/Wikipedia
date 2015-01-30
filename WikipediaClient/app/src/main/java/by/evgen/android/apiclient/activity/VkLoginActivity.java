package by.evgen.android.apiclient.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.auth.VkOAuthHelper;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 30.10.2014.
 */

public class VkLoginActivity extends ActionBarActivity implements VkOAuthHelper.Callbacks {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_login);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new VkWebViewClient());
        mWebView.loadUrl(VkOAuthHelper.AUTORIZATION_URL);
    }

    @Override
    public void onError(Exception e) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(e.getMessage())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    private class VkWebViewClient extends WebViewClient {

        public VkWebViewClient() {
            super();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.text(getClass(), "page started " + url);
            showProgress();
            view.setVisibility(View.INVISIBLE);
        }

       @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
           Log.text(getClass(), "overr " + url);
           if (VkOAuthHelper.proceedRedirectURL(VkLoginActivity.this, url, VkLoginActivity.this)) {
                Log.text(getClass(), "overr redr");
                view.setVisibility(View.INVISIBLE);
                Log.text(getClass(), "Parsing url" + url);
                setResult(RESULT_OK);
                finish();
                return true;
            } else {
                //view.loadUrl(url);
                return false;
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.setVisibility(View.VISIBLE);
            dismissProgress();
            Log.text(getClass(), "error " + failingUrl);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.text(getClass(), "finish " + url);
            if (url.contains("&amp;")) {
                url = url.replace("&amp;", "&");
                Log.text(getClass(), "overr after replace " + url);
                view.loadUrl(url);
                return;
            }
            view.setVisibility(View.VISIBLE);
            dismissProgress();
        }

    }

    private void dismissProgress() {
        findViewById(android.R.id.progress).setVisibility(View.GONE);
    }

    private void showProgress() {
        findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
    }

}
