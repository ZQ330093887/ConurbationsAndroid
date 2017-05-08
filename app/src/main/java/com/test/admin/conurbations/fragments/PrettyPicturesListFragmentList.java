package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.test.admin.conurbations.activitys.IPrettyPictureListView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.PrettyPictureListAdapter;
import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.model.response.NetImage360;
import com.test.admin.conurbations.presenter.PrettyPicturesListPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class PrettyPicturesListFragmentList extends BaseLazyListFragment implements IPrettyPictureListView {

    private Moment.SGImgType range;

    public void setRange(Moment.SGImgType range) {
        this.range = range;
    }

    protected PrettyPicturesListPresenter mPrettyPicturesListPresenter;
    protected PrettyPictureListAdapter mPrettyPictureListAdapter;

    @Override
    protected void initData(Bundle bundle) {
    }

    @Override
    public void setPrettyPictureData(NetImage360 netImage) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (netImage.data == null && netImage.data.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(false);
            mDataList.addAll(netImage.data);
            mPrettyPictureListAdapter.setList(mDataList);
            mPrettyPictureListAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyGridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
    }

    @Override
    protected void refreshList(int page) {
        if (mPrettyPicturesListPresenter != null) {
            mPrettyPicturesListPresenter.getPrettyPictureLisData(page, isRefresh);
            isRefresh = true;
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        mPrettyPictureListAdapter = new PrettyPictureListAdapter();
        return mPrettyPictureListAdapter;
    }

    @Override
    protected void setUpPresenter() {
        mPrettyPicturesListPresenter = new PrettyPicturesListPresenter(this);
    }
}
