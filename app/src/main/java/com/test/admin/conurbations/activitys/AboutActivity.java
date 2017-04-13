package com.test.admin.conurbations.activitys;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.annotations.FindView;

public class AboutActivity extends BaseActivity {
    @FindView
    Toolbar mToolbarToolbar;
    @FindView
    CollapsingToolbarLayout mHeadCollapsingToolbarLayout;
    @FindView
    TextView mVersionNameTextView;

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mToolbarToolbar, "", "");
        mHeadCollapsingToolbarLayout.setTitle(getString(R.string.about));
        mVersionNameTextView.setText(getVersion());
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
