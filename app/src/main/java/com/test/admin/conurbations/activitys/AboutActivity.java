package com.test.admin.conurbations.activitys;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.test.admin.conurbations.R;

import butterknife.Bind;

public class AboutActivity extends BaseActivity {
    @Bind(R.id.toolbar_news_detail_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ctb_about_head)
    CollapsingToolbarLayout mHeadCollapsingToolbarLayout;
    @Bind(R.id.tv_about_version)
    TextView mVersionTextView;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mToolbar, "", "");
        mHeadCollapsingToolbarLayout.setTitle(getString(R.string.about));
        mVersionTextView.setText(getVersion());
    }

    @Override
    protected void initPresenter() {

    }

    private String getVersion() {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return getString(R.string.about_version);
        }
    }
}
