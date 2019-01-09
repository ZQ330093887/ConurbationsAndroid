package com.test.admin.conurbations.activitys;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.databinding.ActivityAboutBinding;

public class AboutActivity extends BaseActivity<ActivityAboutBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mBinding.toolbarAboutToolbar, "", "");
        mBinding.ctlAboutHead.setTitle(getString(R.string.set_about));
        mBinding.tvAboutVersionName.setText(getVersion());
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
