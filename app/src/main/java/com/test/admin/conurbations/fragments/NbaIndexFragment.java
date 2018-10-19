package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.INBAinfoView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.NBAIndexAdapter;
import com.test.admin.conurbations.model.entity.NewsItem;
import com.test.admin.conurbations.model.entity.NewsItemBean;
import com.test.admin.conurbations.presenter.NBAIndexPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2017/4/11.
 */

public class NbaIndexFragment extends BaseLazyListFragment<NewsItemBean, NBAIndexPresenter> implements INBAinfoView {

    private String type;

    public void setType(String type) {
        this.type = type;
    }

    @Inject
    NBAIndexAdapter mNbaIndexAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }

    @Override
    public void setNBAInfoData(NewsItem nbaInfoData) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (nbaInfoData.data == null || nbaInfoData.data.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(true);
            mDataList.addAll(nbaInfoData.data);
            mNbaIndexAdapter.setList(mDataList);
            mNbaIndexAdapter.notifyDataSetChanged();
        }

        recycler.onRefreshCompleted();
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return mNbaIndexAdapter;
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getNBAData(((page - 1) * 10), type, isRefresh);
            isRefresh = true;
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }
}
