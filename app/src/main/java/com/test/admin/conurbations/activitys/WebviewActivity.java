package com.test.admin.conurbations.activitys;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouqiong on 2017/1/4.
 */
public class WebviewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String EXTRA_URL = "URL";
    private static final String EXTRA_TITLE = "TITLE";

    @Bind(R.id.toolbar)
    Toolbar vToolbar;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout vRefreshLayout;
    @Bind(R.id.webview)
    WebView vWebView;

    private String mUrl;
    private String mTitle;

    public static void openUrl(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, WebviewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        ButterKnife.bind(this);
        setSupportActionBar(vToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        vRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        setUpWebView();

        if (null != getIntent()) {
            mUrl = getIntent().getStringExtra(EXTRA_URL);
            mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        }
        setTitle(mTitle);
        vWebView.loadUrl(mUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != vWebView) {
            vWebView.onPause();
        }
    }

    private void setUpWebView() {
        WebSettings settings = vWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        vWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                vRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                vRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                vRefreshLayout.setRefreshing(false);
            }
        });
        vWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 80) {
                    vRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && vWebView.canGoBack()) {
            vWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
                if (vWebView.canGoBack()) {
                    vWebView.goBack();
                    return true;
                }
                break;
            case R.id.action_share:
                sharePage();
                return true;
            case R.id.action_open_in_browser:
                openInBrowser();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sharePage() {
        String title = vWebView.getTitle();
        String url = vWebView.getUrl();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_page, title, url));
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    private void openInBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(vWebView.getUrl());
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        vWebView.reload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != vWebView) {
            // After Android 5.1, there has a problem in Webview:
            // if onDetach is called after destroy, AwComponentCallbacks object will be leaked.
            if (null != vWebView.getParent()) {
                ((ViewGroup) vWebView.getParent()).removeView(vWebView);
            }
            vWebView.destroy();
        }
        ButterKnife.unbind(this);
    }
}
