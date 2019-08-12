package com.test.admin.conurbations.activitys;

import android.os.Bundle;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.databinding.ActivityAboutBinding;
import com.test.admin.conurbations.utils.AppUtils;

public class AboutActivity extends BaseActivity<ActivityAboutBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mBinding.toolbarAboutToolbar, "", "");
        mBinding.ctlAboutHead.setTitle(getString(R.string.set_about));
        mBinding.tvAboutVersionName.setText(AppUtils.getVersion(this));
    }
}
