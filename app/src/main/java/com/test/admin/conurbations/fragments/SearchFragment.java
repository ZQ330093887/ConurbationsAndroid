package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.activitys.ISearchView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.SearchAdapter;
import com.test.admin.conurbations.data.entity.Moment;
import com.test.admin.conurbations.model.NetImage360;
import com.test.admin.conurbations.presenter.SearchPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;


public class SearchFragment extends BaseListFragment implements ISearchView {
    public static final String CLASS_ID = "c_id";
    public static final String CLASS_TITLE = "title";
    private Moment.SGImgType range;
    public void setRange(Moment.SGImgType range) {
        this.range = range;
    }
    protected SearchPresenter testPresenter;
    protected SearchAdapter recommendAdapter;
    private String classId;
    public PullRecycler getRecyclerView() {
        return recycler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        if (getArguments().containsKey(CLASS_ID)) {
            classId = getArguments().getString(CLASS_ID);
        }
        return viewRoot;
    }

    @Override
    public void setSearchData(NetImage360 searchData) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (searchData.data == null && searchData.data.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(false);
            mDataList.addAll(searchData.data);
            recommendAdapter.setList(mDataList);
            recommendAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        testPresenter.getSearchInfo(classId);
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        recommendAdapter = new SearchAdapter();
        return recommendAdapter;
    }

    @Override
    protected void setUpPresenter() {
        testPresenter = new SearchPresenter(this);
    }
}
