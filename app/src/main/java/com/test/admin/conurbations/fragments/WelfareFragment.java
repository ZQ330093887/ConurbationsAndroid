package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IWelfareView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.WelfareListAdapter;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.presenter.WelfarePresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class WelfareFragment extends BaseLazyListFragment implements IWelfareView {
    private Moment.Range range;
    protected WelfareListAdapter mWelfareListAdapter;
    protected WelfarePresenter mWelfarePresenter;

    public void setRange(Moment.Range range) {
        this.range = range;
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
    protected void initData(Bundle bundle) {
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        mWelfareListAdapter = new WelfareListAdapter();
        return mWelfareListAdapter;
    }

    @Override
    protected void setUpPresenter() {
        mWelfarePresenter = new WelfarePresenter(this);
    }

    @Override
    protected void refreshList(int page) {
        if (mWelfarePresenter != null) {
            mWelfarePresenter.getWelfareData(isRefresh, page);
            isRefresh = true;
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }
}
