package com.test.admin.conurbations.activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.databinding.ActivityWebViewBinding;
import com.test.admin.conurbations.utils.FileUtils;
import com.test.admin.conurbations.utils.ToastUtils;

/**
 * Created by zhouqiong on 2017/1/4.
 * Update 2017/5/18
 */
public class WebViewActivity extends BaseActivity<ActivityWebViewBinding> {

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
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mBinding.toolbarWebViewToolbar, "", "");
        getIntentData();
        setTitle(mWebViewTitle);
        isShowToolbar();
        isLoadUrl();
    }

    private void isLoadUrl() {
        mBinding.blWebViewContent.setOverrideUrlLoading(isOverrideUrlLoading);
        if (!TextUtils.isEmpty(mWebViewUrl)) {
            mBinding.blWebViewContent.loadUrl(mWebViewUrl);
        } else {
            ToastUtils.getInstance().showToast("获取URL地址失败");
        }
    }

    private void isShowToolbar() {
        if (!isShowBottomBar) {
            mBinding.blWebViewContent.hideBrowserController();
        } else {
            mBinding.blWebViewContent.showBrowserController();
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
    protected void onPause() {
        if (mBinding.blWebViewContent.getWebView() != null) {
            mBinding.blWebViewContent.getWebView().onPause();
            mBinding.blWebViewContent.getWebView().reload();
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
                if (mBinding.blWebViewContent.canGoBack()) {
                    mBinding.blWebViewContent.goBack();
                    return true;
                }
                break;
            case R.id.action_share:
                FileUtils.sharePage(mBinding.blWebViewContent.getWebView(), getContext());
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
        Uri uri = Uri.parse(mBinding.blWebViewContent.getWebView().getUrl());
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinding.blWebViewContent.getWebView() != null) {
            mBinding.blWebViewContent.getWebView().removeAllViews();
            mBinding.blWebViewContent.getWebView().destroy();
        }
    }
}
