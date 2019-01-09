package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IPrettyPictureListView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.PrettyPicturesAdapter;
import com.test.admin.conurbations.model.entity.TSZImageBean;
import com.test.admin.conurbations.model.response.NetImage360;
import com.test.admin.conurbations.presenter.PrettyPicturesPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class PrettyPicturesFragment extends BaseListFragment<TSZImageBean, PrettyPicturesPresenter>
        implements IPrettyPictureListView {

    public static final String CLASS_ID = "c_id";
    public static final String CLASS_TITLE = "title";

    private String classId;

    @Inject
    PrettyPicturesAdapter mPrettyPicturesAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
        if (getArguments().containsKey(CLASS_ID)) {
            classId = getArguments().getString(CLASS_ID);
        }
    }

    @Override
    protected void loadingData() {
        mBinding.get().refreshLayout.setEnableLoadMore(false);
        if (mPresenter != null) {
            mPresenter.getPrettyPictureLisData(classId);
        }
    }

    @Override
    public void setCacheData(NetImage360 image360) {
    }

    @Override
    public void setPrettyPictureData(NetImage360 netImage) {
        mStatusManager.showSuccessLayout();
        if (netImage == null || netImage.data.size() == 0) {
            if (isRefresh) {
                if (mPrettyPicturesAdapter.list == null || mPrettyPicturesAdapter.list.size() <= 0) {
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
            mPrettyPicturesAdapter.setList(mDataList);
            mPrettyPicturesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getPrettyPictureLisData(classId);
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return mPrettyPicturesAdapter;
    }
}
