package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.INBAInfoView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.NBAIndexAdapter;
import com.test.admin.conurbations.model.entity.NewsItem;
import com.test.admin.conurbations.model.entity.NewsItemBean;
import com.test.admin.conurbations.presenter.NBAIndexPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import javax.inject.Inject;
/**
 * Created by zhouqiong on 2017/4/11.
 */

public class NbaIndexFragment extends BaseSubFragment<NewsItemBean, NBAIndexPresenter> implements INBAInfoView {

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
    protected void loadingData() {
        mPresenter.getCacheData(type);
    }

    @Override
    public void setCacheData(NewsItem nbaInfoData) {
        mStatusManager.showSuccessLayout();
        if (nbaInfoData != null && nbaInfoData.data.size() > 0) {
            mDataList.clear();
            mDataList.addAll(nbaInfoData.data);
            mNbaIndexAdapter.setList(mDataList);
            mNbaIndexAdapter.notifyDataSetChanged();
        } else {
            mStatusManager.showLoadingLayout();
            refreshList(1);
        }
    }

    @Override
    public void setNBAInfoData(NewsItem nbaInfoData) {
        mStatusManager.showSuccessLayout();
        if (nbaInfoData == null || nbaInfoData.data.size() == 0) {
            if (isRefresh) {
                if (mNbaIndexAdapter.list == null || mNbaIndexAdapter.list.size() <= 0) {
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
            mDataList.addAll(nbaInfoData.data);
            mNbaIndexAdapter.setList(mDataList);
            mNbaIndexAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return mNbaIndexAdapter;
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getNBAData(((page - 1) * 10), type);
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }
}
