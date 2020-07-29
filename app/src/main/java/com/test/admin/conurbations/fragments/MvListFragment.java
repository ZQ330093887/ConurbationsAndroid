package com.test.admin.conurbations.fragments;


import android.os.Bundle;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IMvView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.MvListAdapter;
import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.presenter.MvListPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.util.List;

import javax.inject.Inject;

/**
 * VM列表
 * Created by zhouqiong on 2016/9/23.
 */
public class MvListFragment extends BaseSubFragment<MvInfo.MvInfoDetail, MvListPresenter> implements IMvView {

    private String mvType = "rank";

    public void setType(String type) {
        this.mvType = type;
    }

    @Inject
    MvListAdapter mvListAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }


    @Override
    protected void loadingData() {
        mPresenter.getCacheData(mvType);
    }


    @Override
    protected BaseListAdapter setUpAdapter() {
        return mvListAdapter;
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        if (mvType != null && mvType.equals("rank")) {
            return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        } else {
            return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
    }

    @Override
    protected void refreshList(int page) {
        if (mvType.equals("rank")) {
            mPresenter.loadMv((page - 1) * 50, mvType);
        } else {
            mBinding.get().refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
            mBinding.get().refreshLayout.setEnableLoadMore(false);
            mPresenter.loadRecentMv(30, mvType);
        }
    }


    @Override
    public void setCacheData(List<MvInfo.MvInfoDetail> welfareData) {
        mStatusManager.showSuccessLayout();
        if (welfareData != null && welfareData.size() > 0) {
            mDataList.clear();
            mDataList.addAll(welfareData);
            mvListAdapter.setList(mDataList);
            mvListAdapter.notifyDataSetChanged();
        } else {
            mStatusManager.showLoadingLayout();
            refreshList(1);
        }
    }

    @Override
    public void showMvList(List<MvInfo.MvInfoDetail> mvList) {
        mStatusManager.showSuccessLayout();
        if (mvList == null || mvList.size() == 0) {
            if (isRefresh) {
                if (mvListAdapter.list == null || mvListAdapter.list.size() <= 0) {
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
            mDataList.addAll(mvList);
            mvListAdapter.setList(mDataList);
            mvListAdapter.notifyDataSetChanged();
        }
    }
}
