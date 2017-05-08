package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.utils.AppUtils;
import com.test.admin.conurbations.utils.CacheUtils;
import com.test.admin.conurbations.utils.ToastUtils;

public class OtherActivity extends BaseActivity implements View.OnClickListener {
    @FindView
    Toolbar mToolbarToolbar;
    @FindView
    TextView mCacheSizeTextView;
    @FindView
    RelativeLayout mCleanCacheRelativeLayout;

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mToolbarToolbar, "设置", "");
        mCacheSizeTextView.setText(CacheUtils.getCacheSize(this));
        mCleanCacheRelativeLayout.setOnClickListener(this);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_other_clean_cache) {
            ACache cache = ACache.get(AppUtils.getAppContext());
            cache.clear();
            CacheUtils.cleanApplicationCache(this);
            mCacheSizeTextView.setText(CacheUtils.getCacheSize(this));
            ToastUtils.getInstance().showToast("缓存清理成功！");
            ACache.get(AppUtils.getAppContext());
        }
    }
}
