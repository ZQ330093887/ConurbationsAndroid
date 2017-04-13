package com.test.admin.conurbations.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.utils.FileUtil;

/**
 * Created by zhouqiong on 2017/1/4.
 */
public class WebViewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @FindView
    Toolbar mToolbarToolbar;
    @FindView
    SwipeRefreshLayout mContentSwipeRefreshLayout;
    @FindView
    WebView mContentWebView;

    private String mWebViewUrl;
    private String mWebViewTitle;

    public static void openUrl(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        activity.startActivity(intent);
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mToolbarToolbar, "", "");
        mContentSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        setUpWebView();

        if (null != getIntent()) {
            mWebViewUrl = getIntent().getStringExtra(EXTRA_URL);
            mWebViewTitle = getIntent().getStringExtra(EXTRA_TITLE);
        }
        setTitle(mWebViewTitle);
        mContentWebView.loadUrl(mWebViewUrl);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mContentWebView) {
            mContentWebView.onPause();
        }
    }

    private void setUpWebView() {
        WebSettings settings = mContentWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        mContentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mContentSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mContentSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                mContentSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mContentWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 80) {
                    mContentSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mContentWebView.canGoBack()) {
                    mContentWebView.goBack();
                    return true;
                }
                break;
            case R.id.action_share:
                FileUtil.sharePage(mContentWebView, getContext());
                return true;
            case R.id.action_open_in_browser:
                openInBrowser();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void openInBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(mContentWebView.getUrl());
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mContentWebView.reload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mContentWebView) {
            // After Android 5.1, there has a problem in Webview:
            // if onDetach is called after destroy, AwComponentCallbacks object will be leaked.
            if (null != mContentWebView.getParent()) {
                ((ViewGroup) mContentWebView.getParent()).removeView(mContentWebView);
            }
            mContentWebView.destroy();
        }
    }
}
