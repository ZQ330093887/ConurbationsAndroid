package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentAdapter;
import com.test.admin.conurbations.databinding.ActivityNudePhotosBinding;
import com.test.admin.conurbations.fragments.NudePhotosFragment;
import com.test.admin.conurbations.model.entity.MenuModel;
import com.test.admin.conurbations.model.entity.PageModel;
import com.test.admin.conurbations.presenter.NudePhotosPresenter;
import com.test.admin.conurbations.utils.StatusBarUtils;
import com.test.admin.conurbations.widget.statuslayoutmanage.StatusLayoutManager;

import java.util.List;


/**
 * Created by zhouqiong on 17/3/17.
 */
public class NudePhotosActivity extends BaseActivity<ActivityNudePhotosBinding> implements INudePhotosView {
    private StatusLayoutManager mStatusManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nude_photos;
    }

    @Override
    protected void initData(Bundle bundle) {

        StatusBarUtils.setWindowStatusBarColor(this, R.color.color_FFC1C1);
        initToolbar(mBinding.toolbar, "肉肉", "");

        initEmptyView();
        RequestTabData();
    }

    private void initEmptyView() {
        mStatusManager = new StatusLayoutManager.Builder(mBinding.mainContent)
                .setDefaultEmptyClickViewVisible(false)
                .build();
        mStatusManager.showLoadingLayout();
    }


    private void RequestTabData() {
        NudePhotosPresenter mPresenter = new NudePhotosPresenter(this);
        mPresenter.getNodeTitle();
    }


    @Override
    public void setNodePhotoData(List<MenuModel> list) {
        runOnUiThread(() -> {
            if (list != null && list.size() > 0) {
                mStatusManager.showSuccessLayout();
                initView(list);
            } else {
                mStatusManager.showEmptyLayout();
            }
        });

    }

    @Override
    public void setNodeDetailData(PageModel pageModel) {
        //该接口这里不做处理，在详情里操作
    }

    @Override
    public void setCacheNudePhotos(PageModel pageModel) {
        //该接口这里不做处理，在详情里操作
    }

    private void initView(List<MenuModel> modelList) {

        Fragment[] mFragments = new Fragment[modelList.size()];
        String[] mTitles = new String[modelList.size()];

        for (int i = 0; i < modelList.size(); i++) {
            MenuModel menuModel = modelList.get(i);
            mTitles[i] = menuModel.title;
            mFragments[i] = new NudePhotosFragment();
            ((NudePhotosFragment) mFragments[i]).setRange(menuModel.url, menuModel.title);
        }

        FragmentAdapter fa = new FragmentAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mBinding.mViewpager.setAdapter(fa);
        mBinding.tabs.setupWithViewPager(mBinding.mViewpager);
        mBinding.mViewpager.setOffscreenPageLimit(2);
        mBinding.mViewpager.setCurrentItem(0);
    }

    @Override
    public void showError(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusManager.showErrorLayout();
            }
        });
    }
}
