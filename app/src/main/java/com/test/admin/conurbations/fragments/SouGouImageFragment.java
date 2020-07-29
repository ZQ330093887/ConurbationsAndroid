package com.test.admin.conurbations.fragments;


import android.os.Bundle;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.ISouGouImageView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.SouGouImageAdapter;
import com.test.admin.conurbations.model.entity.SoGouSearcher;
import com.test.admin.conurbations.model.response.NetImage;
import com.test.admin.conurbations.presenter.SouGouImagePresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class SouGouImageFragment extends BaseSubFragment<SoGouSearcher, SouGouImagePresenter>
        implements ISouGouImageView {

    private String range;

    public void setRange(String range) {
        this.range = range;
    }

    @Inject
    SouGouImageAdapter mSouGouImageAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }


    @Override
    protected void loadingData() {
        mPresenter.getCacheData(range);
    }

    @Override
    public void setCacheData(NetImage imageData) {
        mStatusManager.showSuccessLayout();
        if (imageData != null && imageData.items.size() > 0) {
            mDataList.clear();
            mDataList.addAll(imageData.items);
            mSouGouImageAdapter.setList(mDataList);
            mSouGouImageAdapter.notifyDataSetChanged();
        } else {
            mStatusManager.showLoadingLayout();
            refreshList(1);
        }
    }

    @Override
    public void setSouGouImageData(NetImage imageData) {
        mStatusManager.showSuccessLayout();
        if (imageData.items == null || imageData.items.size() == 0) {
            if (isRefresh) {
                if (mSouGouImageAdapter.list == null || mSouGouImageAdapter.list.size() <= 0) {
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
            mDataList.addAll(imageData.items);
            mSouGouImageAdapter.setList(mDataList);
            mSouGouImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getSouGouImageData(range, page);
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return mSouGouImageAdapter;
    }
}
