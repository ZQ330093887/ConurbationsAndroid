package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.activitys.ISouGouImageList;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.SouGouImgListAdapter;
import com.test.admin.conurbations.model.NetImage;
import com.test.admin.conurbations.presenter.DayAndDayImagePresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;


public class SouGouImageFragment extends BaseListFragment implements ISouGouImageList {

    private String range;
    public void setRange(String range) {
        this.range = range;
    }

    DayAndDayImagePresenter dayImagePresenter;
    SouGouImgListAdapter souGouImgListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        return viewRoot;
    }

    @Override
    public void setSouGouImage(NetImage souGouImage) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (souGouImage.items == null || souGouImage.items.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(true);
            mDataList.addAll(souGouImage.items);
            souGouImgListAdapter.setList(mDataList);
            souGouImgListAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        dayImagePresenter.getWelfareData(range, page);
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        souGouImgListAdapter = new SouGouImgListAdapter();
        return souGouImgListAdapter;
    }

    @Override
    protected void setUpPresenter() {
        dayImagePresenter = new DayAndDayImagePresenter(this);
    }
}
