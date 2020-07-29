package com.test.admin.conurbations.fragments;

import android.os.Bundle;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IWelfareView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.GanHuoAdapter;
import com.test.admin.conurbations.model.entity.GanHuoDataBean;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.presenter.GanHuoPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class GanHuoFragment extends BaseSubFragment<GanHuoDataBean, GanHuoPresenter> implements IWelfareView {

    private String range;

    @Inject
    GanHuoAdapter mGanHuoAdapter;

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void loadingData() {
        mPresenter.getCacheData(range);
    }

    @Override
    public void setCacheData(GankData welfareData) {
        mStatusManager.showSuccessLayout();
        if (welfareData != null && welfareData.data.size() > 0) {
            mDataList.clear();
            mDataList.addAll(welfareData.data);
            mGanHuoAdapter.setList(mDataList);
            mGanHuoAdapter.notifyDataSetChanged();
        } else {
            mStatusManager.showLoadingLayout();
            refreshList(1);
        }
    }

    @Override
    public void setWelfareData(GankData welfareData) {
        mStatusManager.showSuccessLayout();
        if (welfareData.data == null || welfareData.data.size() == 0) {
            if (isRefresh) {
                if (mGanHuoAdapter.list == null || mGanHuoAdapter.list.size() <= 0) {
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
            mDataList.addAll(welfareData.data);
            mGanHuoAdapter.setList(mDataList);
            mGanHuoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return mGanHuoAdapter;
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getWelfareData(range, page);
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }
}
