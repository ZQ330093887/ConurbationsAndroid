package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.activitys.ISearchView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.SearchAdapter;
import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.model.response.NetImage;
import com.test.admin.conurbations.presenter.SearchPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class SearchFragment extends BaseListFragment implements ISearchView {
    public static final String CLASS_SEARCH = "search_query";
    private Moment.SGImgType range;

    public void setRange(Moment.SGImgType range) {
        this.range = range;
    }

    protected SearchPresenter mSearchPresenter;
    protected SearchAdapter mSearchAdapter;
    private String mSearchQuery;

    public PullRecycler getRecyclerView() {
        return recycler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        if (getArguments().containsKey(CLASS_SEARCH)) {
            mSearchQuery = getArguments().getString(CLASS_SEARCH);
        }
        return viewRoot;
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
        mSearchPresenter.getSearchQueryInfo(mSearchQuery, page);
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        mSearchAdapter = new SearchAdapter();
        return mSearchAdapter;
    }

    @Override
    protected void setUpPresenter() {
        mSearchPresenter = new SearchPresenter(this);
    }
}
