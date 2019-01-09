package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.view.View;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.databinding.ActivityOtherBinding;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.utils.AppUtils;
import com.test.admin.conurbations.utils.CacheUtils;
import com.test.admin.conurbations.utils.ToastUtils;

public class OtherActivity extends BaseActivity<ActivityOtherBinding> implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_other;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mBinding.toolbarOtherToolbar, "设置", "");
        mBinding.tvOtherCacheSize.setText(CacheUtils.getCacheSize(this));
        mBinding.rlOtherCleanCache.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_other_clean_cache) {
            ACache cache = ACache.get(AppUtils.getAppContext());
            cache.clear();
            CacheUtils.cleanApplicationCache(this);
            mBinding.tvOtherCacheSize.setText(CacheUtils.getCacheSize(this));
            ToastUtils.getInstance().showToast("缓存清理成功！");
            ACache.get(AppUtils.getAppContext());
        }
    }
}
