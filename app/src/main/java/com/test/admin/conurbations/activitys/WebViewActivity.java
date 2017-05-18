package com.test.admin.conurbations.activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.utils.FileUtil;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.widget.BrowserLayout;

/**
 * Created by zhouqiong on 2017/1/4.
 * Update 2017/5/18
 */
public class WebViewActivity extends BaseActivity {

    @FindView
    Toolbar mToolbarToolbar;
    @FindView
    BrowserLayout mContentBrowserLayout;

    private String mWebViewUrl;
    private String mWebViewTitle;
    private boolean isShowBottomBar = true;
    private boolean isOverrideUrlLoading = true;

    public static void openUrl(Context context, String url, String title, boolean isShowBottomBar, boolean isOverrideUrlLoading) {
        Intent intent = new Intent(context, WebViewActivity.class)
                .putExtra(EXTRA_URL, url)
                .putExtra(EXTRA_TITLE, title)
                .putExtra(BUNDLE_KEY_SHOW_BOTTOM_BAR, isShowBottomBar)
                .putExtra(BUNDLE_OVERRIDE, isOverrideUrlLoading);
        context.startActivity(intent);
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mToolbarToolbar, "", "");
        getIntentData();
        setTitle(mWebViewTitle);
        isShowToolbar();
        isLoadUrl();
    }

    private void isLoadUrl() {
        mContentBrowserLayout.setOverrideUrlLoading(isOverrideUrlLoading);
        if (!TextUtils.isEmpty(mWebViewUrl)) {
            mContentBrowserLayout.loadUrl(mWebViewUrl);
        } else {
            ToastUtils.getInstance().showToast("获取URL地址失败");
        }
    }

    private void isShowToolbar() {
        if (!isShowBottomBar) {
            mContentBrowserLayout.hideBrowserController();
        } else {
            mContentBrowserLayout.showBrowserController();
        }
    }

    private void getIntentData() {
        if (null != getIntent()) {
            mWebViewUrl = getIntent().getStringExtra(EXTRA_URL);
            mWebViewTitle = getIntent().getStringExtra(EXTRA_TITLE);
            isShowBottomBar = getIntent().getBooleanExtra(BUNDLE_KEY_SHOW_BOTTOM_BAR, true);
            isOverrideUrlLoading = getIntent().getBooleanExtra(BUNDLE_OVERRIDE, true);
        }
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void onPause() {
        if (mContentBrowserLayout.getWebView() != null) {
            mContentBrowserLayout.getWebView().onPause();
            mContentBrowserLayout.getWebView().reload();
        }
        super.onPause();
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
                if (mContentBrowserLayout.canGoBack()) {
                    mContentBrowserLayout.goBack();
                    return true;
                }
                break;
            case R.id.action_share:
                FileUtil.sharePage(mContentBrowserLayout.getWebView(), getContext());
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
        Uri uri = Uri.parse(mContentBrowserLayout.getWebView().getUrl());
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContentBrowserLayout.getWebView() != null) {
            mContentBrowserLayout.getWebView().removeAllViews();
            mContentBrowserLayout.getWebView().destroy();
        }
    }
}
