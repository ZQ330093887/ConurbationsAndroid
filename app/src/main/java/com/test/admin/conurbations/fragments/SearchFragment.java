package com.test.admin.conurbations.fragments;


import android.os.Bundle;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.ISearchView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.SearchAdapter;
import com.test.admin.conurbations.model.entity.SoGouSearcher;
import com.test.admin.conurbations.model.response.NetImage;
import com.test.admin.conurbations.presenter.SearchPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class SearchFragment extends BaseListFragment<SoGouSearcher, SearchPresenter> implements ISearchView {
    public static final String CLASS_SEARCH = "search_query";
    private String mSearchQuery;

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
    protected void loadingData() {
        if (mPresenter != null) {
            mPresenter.getSearchQueryInfo(mSearchQuery, 1);
        }
    }

    @Override
    public void setSearchData(NetImage searchData) {
        mStatusManager.showSuccessLayout();
        if (searchData == null || searchData.items.size() == 0) {
            if (isRefresh) {
                if (mSearchAdapter.list == null || mSearchAdapter.list.size() <= 0) {
                    mStatusManager.showEmptyLayout();
                }
            } else {
                if (page > 1) {
                    mBinding.get().refreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
        } else {
            if (isRefresh) {
                mDataList.clear();
            } else {
                mBinding.get().refreshLayout.finishLoadMore(true);
            }
            mDataList.addAll(searchData.items);
            mSearchAdapter.setList(mDataList);
            mSearchAdapter.notifyDataSetChanged();
        }
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
