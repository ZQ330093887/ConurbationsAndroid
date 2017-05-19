package com.test.admin.conurbations.fragments;

import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IVideoLiveView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.MatchVideoLiveListAdapter;
import com.test.admin.conurbations.model.response.VideoLiveData;
import com.test.admin.conurbations.presenter.MatchVideoLivePresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

public class MatchVideoLiveListFragment extends BaseListFragment implements IVideoLiveView {
    protected MatchVideoLivePresenter matchVideoLivePresenter;
    protected MatchVideoLiveListAdapter matchVideoLiveListAdapter;

    @Override
    public void setVideoLiveData(VideoLiveData liveInfo) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (liveInfo.items == null && liveInfo.items.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(false);
            mDataList.addAll(liveInfo.items);
            matchVideoLiveListAdapter.setList(mDataList);
            matchVideoLiveListAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        if (matchVideoLivePresenter != null) {
            matchVideoLivePresenter.getVideoLiveInfo();
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        matchVideoLiveListAdapter = new MatchVideoLiveListAdapter();
        return matchVideoLiveListAdapter;
    }

    @Override
    protected void setUpPresenter() {
        matchVideoLivePresenter = new MatchVideoLivePresenter(this);
    }
}
