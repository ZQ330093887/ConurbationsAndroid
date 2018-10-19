package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IWelfareView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.WelfareListAdapter;
import com.test.admin.conurbations.model.entity.GanHuoDataBean;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.presenter.WelfarePresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class WelfareFragment extends BaseLazyListFragment<GanHuoDataBean, WelfarePresenter> implements IWelfareView {
    private Moment.Range range;

    public void setRange(Moment.Range range) {
        this.range = range;
    }

    @Inject
    WelfareListAdapter mWelfareListAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }

    @Override
    public void setWelfareData(GankData welfareData) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (welfareData.results == null || welfareData.results.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(true);
            mDataList.addAll(welfareData.results);
            mWelfareListAdapter.setList(mDataList);
            mWelfareListAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return mWelfareListAdapter;
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getWelfareData(isRefresh, page);
            isRefresh = true;
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }
}
