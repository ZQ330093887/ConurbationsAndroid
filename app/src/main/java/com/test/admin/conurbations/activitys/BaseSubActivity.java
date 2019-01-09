package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.databinding.ActivityBaseSubBinding;
import com.test.admin.conurbations.di.component.ActivityComponent;
import com.test.admin.conurbations.di.component.DaggerActivityComponent;
import com.test.admin.conurbations.di.module.ActivityModule;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.presenter.BasePresenter;
import com.test.admin.conurbations.utils.SaveBitmapUtils;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;
import com.test.admin.conurbations.widget.SolidApplication;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2017/5/3.
 */

public abstract class BaseSubActivity<T, P extends BasePresenter> extends BaseActivity<ActivityBaseSubBinding>
        implements PullRecycler.OnRecyclerRefreshListener {
    /**
     * 初始化数据
     */
    public int action;
    private int page = 1;
    protected List<T> mDataList;
    private View mHeaderView;

    public int limit = 10;
    /**
     * isRefresh这个属性是添加缓存功能的时候用到的，我的缓存思路是：第一次进入界面走缓存，缓存为空的情况下
     * 网络请求数据，然后缓存起来，这个时候我需要一个属性判断是否需要刷新，之前在做刷新逻辑的时候没有将是否
     * 需要刷新暴露出来，让开发者自己选，这是一个小小的失误，（这里用isRefresh默认不刷新，除非用户调用了刷新一行才会刷新）
     */
    public boolean isRefresh = false;

    @Inject
    protected P mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_sub;
    }

    @Override
    protected void initData(Bundle bundle) {
        initView();
        initRecycler();
        mPresenter.attachView(this);
    }

    private void initRecycler() {
        mBinding.listView.setRefreshing();
        mBinding.listView.setOnRefreshListener(this);
        mBinding.listView.setLayoutManager(getLayoutManager());
        mBinding.listView.setAdapter(setUpAdapter());
    }

    @Override
    public void onRefresh(int action) {
        this.action = action;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }
        refreshList(page++);
    }

    protected abstract ILayoutManager getLayoutManager();

    protected abstract void refreshList(int page);

    protected abstract void initView();

    protected abstract BaseListAdapter setUpAdapter();


    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(SolidApplication.getInstance().getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void detachView() {

    }

    /*******************recycleView设置header默认的写了一个放这里，可自定义********************/
    public View getRecycleHeaderView(NewsList playlist) {
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.activity_online_header, null);
        showHeaderInfo(mHeaderView, playlist);
        return mHeaderView;
    }

    private void showHeaderInfo(View mHeaderView, NewsList playlist) {
        if (playlist != null) {
            ImageView mIvCover = mHeaderView.findViewById(R.id.iv_cover);
            TextView mTvTitle = mHeaderView.findViewById(R.id.tv_title);
            TextView mTvDesc = mHeaderView.findViewById(R.id.tv_comment);
            ImageView mIvBackground = mHeaderView.findViewById(R.id.coverBgIv);

            SaveBitmapUtils.loadBitmap(this, playlist.coverUrl, bitmap -> {
                mIvCover.setImageBitmap(bitmap);
                mIvBackground.setImageBitmap(bitmap);
                mIvBackground.setImageDrawable(SaveBitmapUtils.createBlurredImageFromBitmap(bitmap));
            });

            mTvTitle.setText(playlist.name);
            mTvDesc.setText(playlist.des);
        }
    }
}
