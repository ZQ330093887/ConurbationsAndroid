package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IWelfareView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.WelfareListAdapter;
import com.test.admin.conurbations.model.entity.GanHuoDataBean;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.presenter.WelfarePresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class WelfareFragment extends BaseSubFragment<GanHuoDataBean, WelfarePresenter> implements IWelfareView {
    private Moment.Range range;

    public void setRange(Moment.Range range) {
        this.range = range;
    }

    @Inject
    WelfareListAdapter mWelfareListAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void loadingData() {
        mPresenter.getCacheData();
    }

    @Override
    public void setCacheData(GankData welfareData) {
        //从缓存中获取数据
        mStatusManager.showSuccessLayout();
        if (welfareData != null && welfareData.data.size() > 0) {
            mDataList.clear();
            mDataList.addAll(welfareData.data);
            mWelfareListAdapter.setList(mDataList);
            mWelfareListAdapter.notifyDataSetChanged();
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
                if (mWelfareListAdapter.list == null || mWelfareListAdapter.list.size() <= 0) {
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
            mWelfareListAdapter.setList(mDataList);
            mWelfareListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getWelfareData(page);
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return mWelfareListAdapter;
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }
}
