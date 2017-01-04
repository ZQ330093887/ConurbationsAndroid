package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.activitys.IWelfareList;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.WelfareListAdapter;
import com.test.admin.conurbations.data.entity.Moment;
import com.test.admin.conurbations.data.response.GankData;
import com.test.admin.conurbations.presenter.WelfarePresenter;
import com.test.admin.conurbations.widget.PullRecycler;

public class WelfareFragment extends BaseListFragment implements IWelfareList {
    private Moment.Range range;
    protected WelfareListAdapter adapter;
    protected WelfarePresenter presenter;
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
        adapter =  new WelfareListAdapter();
        return adapter;
    }

    @Override
    protected void setUpPresenter() {
        presenter = new WelfarePresenter(this);
    }

    @Override
    protected void refreshList(int page) {
        presenter.getWelfareData(page);
    }
}
