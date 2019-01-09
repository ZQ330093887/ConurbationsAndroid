package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.databinding.ActivityDownloadBinding;
import com.test.admin.conurbations.fragments.DownloadFragment;
import com.test.admin.conurbations.fragments.NewsInfoListDetailFragment;
import com.test.admin.conurbations.utils.StatusBarUtils;


/**
 * Created by zhouqiong on 17/3/17.
 */
public class DownloadActivity extends BaseActivity<ActivityDownloadBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    protected void initData(Bundle bundle) {
        boolean isCache = getIntent().getBooleanExtra("is_cache", false);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.theme_primary );
        if (bundle == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_down_container, DownloadFragment.newInstance(isCache))
                    .commit();
        }
    }
}
