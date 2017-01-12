package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.activitys.IWelfareList;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.GanHuoListAdapter;
import com.test.admin.conurbations.data.response.GankData;
import com.test.admin.conurbations.presenter.GanHuoListPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

public class GanHuoListFragment extends BaseListFragment implements IWelfareList {
    private String range;
    protected GanHuoListAdapter adapter;
    protected GanHuoListPresenter presenter;

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
            adapter.setList(mDataList);
            adapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        return viewRoot;
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        adapter = new GanHuoListAdapter();
        return adapter;
    }

    @Override
    protected void setUpPresenter() {
        presenter = new GanHuoListPresenter(this);
    }

    @Override
    protected void refreshList(int page) {
        presenter.getWelfareData(range, page);
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }
}
