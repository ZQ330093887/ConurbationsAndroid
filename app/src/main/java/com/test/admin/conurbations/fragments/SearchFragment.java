package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.ISearchView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.SearchAdapter;
import com.test.admin.conurbations.model.entity.SosoSearcher;
import com.test.admin.conurbations.model.response.NetImage;
import com.test.admin.conurbations.presenter.SearchPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class SearchFragment extends BaseListFragment<SosoSearcher, SearchPresenter> implements ISearchView {
    public static final String CLASS_SEARCH = "search_query";
    private String mSearchQuery;

    public PullRecycler getRecyclerView() {
        return recycler;
    }

    @Inject
    SearchAdapter mSearchAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
        if (getArguments().containsKey(CLASS_SEARCH)) {
            mSearchQuery = getArguments().getString(CLASS_SEARCH);
        }
    }

    @Override
    public void setSearchData(NetImage searchData) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (searchData.items == null && searchData.items.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(false);
            mDataList.addAll(searchData.items);
            mSearchAdapter.setList(mDataList);
            mSearchAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getSearchQueryInfo(mSearchQuery, page);
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return mSearchAdapter;
    }
}
