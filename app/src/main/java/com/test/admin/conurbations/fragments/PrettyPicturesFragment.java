package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IPrettyPictureListView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.PrettyPicturesAdapter;
import com.test.admin.conurbations.model.response.NetImage360;
import com.test.admin.conurbations.presenter.PrettyPicturesPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class PrettyPicturesFragment extends BaseListFragment implements IPrettyPictureListView {
    public static final String CLASS_ID = "c_id";
    public static final String CLASS_TITLE = "title";

    protected PrettyPicturesPresenter mPrettyPicturesPresenter;
    protected PrettyPicturesAdapter mPrettyPicturesAdapter;
    private String classId;

    public PullRecycler getRecyclerView() {
        return recycler;
    }

    @Override
    protected void initData(Bundle bundle) {
        if (getArguments().containsKey(CLASS_ID)) {
            classId = getArguments().getString(CLASS_ID);
        }
    }

    @Override
    public void setPrettyPictureData(NetImage360 pictureMoreData) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (pictureMoreData.data == null && pictureMoreData.data.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(false);
            mDataList.addAll(pictureMoreData.data);
            mPrettyPicturesAdapter.setList(mDataList);
            mPrettyPicturesAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        if (mPrettyPicturesPresenter != null) {
            mPrettyPicturesPresenter.getPrettyPictureLisData(classId, page);
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        mPrettyPicturesAdapter = new PrettyPicturesAdapter();
        return mPrettyPicturesAdapter;
    }

    @Override
    protected void setUpPresenter() {
        mPrettyPicturesPresenter = new PrettyPicturesPresenter(this);
    }
}
