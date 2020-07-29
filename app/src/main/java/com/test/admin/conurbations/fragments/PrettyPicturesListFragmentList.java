package com.test.admin.conurbations.fragments;


import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;

import com.test.admin.conurbations.activitys.IPrettyPictureListView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.PrettyPictureListAdapter;
import com.test.admin.conurbations.model.entity.TSZImageBean;
import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.model.response.NetImage360;
import com.test.admin.conurbations.presenter.PrettyPicturesListPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyGridLayoutManager;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class PrettyPicturesListFragmentList extends BaseSubFragment<TSZImageBean, PrettyPicturesListPresenter> implements IPrettyPictureListView {

    private Moment.SGImgType range;

    public void setRange(Moment.SGImgType range) {
        this.range = range;
    }

    @Inject
    PrettyPictureListAdapter mPrettyPictureListAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }


    @Override
    protected void loadingData() {
        mBinding.get().refreshLayout.setEnableLoadMore(false);
        mPresenter.getCacheData();
    }

    @Override
    public void setCacheData(NetImage360 image360) {
        mStatusManager.showSuccessLayout();
        if (image360 != null && image360.data.size() > 0) {
            mDataList.clear();
            mDataList.addAll(image360.data);
            mPrettyPictureListAdapter.setList(mDataList);
            mPrettyPictureListAdapter.notifyDataSetChanged();
        } else {
            mStatusManager.showLoadingLayout();
            refreshList(1);
        }
    }

    @Override
    public void setPrettyPictureData(NetImage360 netImage) {
        mStatusManager.showSuccessLayout();
        if (netImage == null || netImage.data.size() == 0) {
            if (isRefresh) {
                if (mPrettyPictureListAdapter.list == null || mPrettyPictureListAdapter.list.size() <= 0) {
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
            mDataList.addAll(netImage.data);
            mPrettyPictureListAdapter.setList(mDataList);
            mPrettyPictureListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyGridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getPrettyPictureLisData(page);
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return mPrettyPictureListAdapter;
    }
}
