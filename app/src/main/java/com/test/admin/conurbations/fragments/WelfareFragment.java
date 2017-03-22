package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.activitys.IWelfareView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.WelfareListAdapter;
import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.presenter.WelfarePresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;
/**
 * Created by zhouqiong on 2016/9/23.
 */
public class WelfareFragment extends BaseListFragment implements IWelfareView {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        return viewRoot;
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        mWelfareListAdapter =  new WelfareListAdapter();
        return mWelfareListAdapter;
    }

    @Override
    protected void setUpPresenter() {
        mWelfarePresenter = new WelfarePresenter(this);
    }

    @Override
    protected void refreshList(int page) {
        mWelfarePresenter.getWelfareData(page);
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }
}
