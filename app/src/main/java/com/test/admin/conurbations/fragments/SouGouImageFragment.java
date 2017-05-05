package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.ISouGouImageView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.SouGouImageAdapter;
import com.test.admin.conurbations.model.response.NetImage;
import com.test.admin.conurbations.presenter.SouGouImagePresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class SouGouImageFragment extends BaseListFragment implements ISouGouImageView {

    private String range;
    public void setRange(String range) {
        this.range = range;
    }

    protected SouGouImagePresenter mSouGouImagePresenter;
    protected SouGouImageAdapter mSouGouImageAdapter;

    public PullRecycler getRecyclerView() {
        return recycler;
    }

    @Override
    protected void initData(Bundle bundle) {}

    @Override
    public void setSouGouImageData(NetImage imageData) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (imageData.items == null || imageData.items.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(true);
            mDataList.addAll(imageData.items);
            mSouGouImageAdapter.setList(mDataList);
            mSouGouImageAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        if (mSouGouImagePresenter != null){
            mSouGouImagePresenter.getSouGouImageData(range, page);
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        mSouGouImageAdapter = new SouGouImageAdapter();
        return mSouGouImageAdapter;
    }

    @Override
    protected void setUpPresenter() {
        mSouGouImagePresenter = new SouGouImagePresenter(this);
    }
}
