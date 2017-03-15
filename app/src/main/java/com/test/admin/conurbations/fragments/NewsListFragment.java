package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.activitys.INewInformationView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.NewsInfoListAdapter;
import com.test.admin.conurbations.model.NewsList;
import com.test.admin.conurbations.presenter.NewsInfoListPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

/**
 * Created by Eric on 2017/3/09.
 */
public class NewsListFragment extends BaseListFragment implements INewInformationView {

    private String tabId;
    protected NewsInfoListAdapter mInformationListAdapter;
    protected NewsInfoListPresenter mNewsInfoListPresenter;

    public void setTable(String tabId) {
        this.tabId = tabId;
    }

    @Override
    public void setNewInfoData(NewsList informationBean) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (informationBean == null || informationBean.stories.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(true);
            mDataList.addAll(informationBean.stories);
            mInformationListAdapter.setList(mDataList);
            mInformationListAdapter.notifyDataSetChanged();
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
        mInformationListAdapter = new NewsInfoListAdapter();
        return mInformationListAdapter;
    }

    @Override
    protected void setUpPresenter() {
        mNewsInfoListPresenter = new NewsInfoListPresenter(this);
    }

    @Override
    protected void refreshList(int page) {
        mNewsInfoListPresenter.getNewsInfoData(tabId);
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }
}
