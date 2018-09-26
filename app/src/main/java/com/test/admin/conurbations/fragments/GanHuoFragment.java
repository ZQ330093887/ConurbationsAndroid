package com.test.admin.conurbations.fragments;

import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IWelfareView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.GanHuoAdapter;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.presenter.GanHuoPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class GanHuoFragment extends BaseLazyListFragment implements IWelfareView {
    private String range;
    protected GanHuoAdapter mGanHuoAdapter;
    protected GanHuoPresenter mGanHuoPresenter;

    public void setRange(String range) {
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
            mGanHuoAdapter.setList(mDataList);
            mGanHuoAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        mGanHuoAdapter = new GanHuoAdapter();
        return mGanHuoAdapter;
    }

    @Override
    protected void setUpPresenter() {
        mGanHuoPresenter = new GanHuoPresenter(this);
    }

    @Override
    protected void refreshList(int page) {
        if (mGanHuoPresenter != null) {
            mGanHuoPresenter.getWelfareData(range, page, isRefresh);
            isRefresh = true;
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public void detachView() {
        if (mGanHuoPresenter != null) {
            mGanHuoPresenter.detachView();
        }
    }
}
