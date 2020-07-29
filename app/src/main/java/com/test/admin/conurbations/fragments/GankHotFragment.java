package com.test.admin.conurbations.fragments;


import android.os.Bundle;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IGankDayView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.GankHotAdapter;
import com.test.admin.conurbations.model.response.GankItem;
import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.presenter.GankDayPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class GankHotFragment extends BaseSubFragment<List<GankItem>, GankDayPresenter> implements IGankDayView {

    private Moment.Range range;

    public void setRange(Moment.Range range) {
        this.range = range;
    }

    @Inject
    GankHotAdapter mGankHotAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }


    @Override
    protected void loadingData() {
        mBinding.get().refreshLayout.setEnableLoadMore(false);
        mPresenter.getCacheData();
    }

    @Override
    public void setCacheData(List<GankItem> todayData) {
        mStatusManager.showSuccessLayout();
        if (todayData != null && todayData.size() > 0) {
            mDataList.clear();
            mDataList.add(todayData);
            mGankHotAdapter.setList(mDataList);
            mGankHotAdapter.notifyDataSetChanged();
        } else {
            mStatusManager.showLoadingLayout();
            refreshList(1);
        }
    }

    @Override
    public void setGankDayData(List<GankItem> items) {
        mStatusManager.showSuccessLayout();

        if (items.size() == 0) {
            mStatusManager.showEmptyLayout();
        } else {
            mDataList.clear();
            mDataList.add(items);
            mGankHotAdapter.setList(mDataList);
            mGankHotAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected BaseListAdapter setUpAdapter() {
        return mGankHotAdapter;
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getDayData();
        }
    }
}
