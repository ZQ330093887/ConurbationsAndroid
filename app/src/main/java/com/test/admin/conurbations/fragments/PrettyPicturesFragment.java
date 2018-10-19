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
import com.test.admin.conurbations.widget.PullRecycler;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class PrettyPicturesFragment extends BaseListFragment<TSZImageBean, PrettyPicturesPresenter> implements IPrettyPictureListView {
    public static final String CLASS_ID = "c_id";
    public static final String CLASS_TITLE = "title";

    private String classId;

    public PullRecycler getRecyclerView() {
        return recycler;
    }

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
        if (mPresenter != null) {
            mPresenter.getPrettyPictureLisData(classId, page);
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return mPrettyPicturesAdapter;
    }
}
