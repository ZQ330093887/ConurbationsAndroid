package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IVideoInfoView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.VideoIndexAdapter;
import com.test.admin.conurbations.model.entity.DouyinVideoListData;
import com.test.admin.conurbations.model.entity.LeVideoData;
import com.test.admin.conurbations.presenter.VideoIndexPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2019/4/2.
 */

public class VideoIndexFragment extends BaseSubFragment<LeVideoData, VideoIndexPresenter>
        implements IVideoInfoView {
    private int max_cursor = 0;

    @Inject
    VideoIndexAdapter videoIndexAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void loadingData() {
        if (mPresenter != null) {
            mPresenter.getDouYinData(getActivity(), max_cursor);
        }
    }


    @Override
    public void setCacheData(DouyinVideoListData douYinData) {
        refreshList(max_cursor);
    }

    @Override
    public void setVideoDouYinData(DouyinVideoListData douYinData) {
        mStatusManager.showSuccessLayout();

        if (douYinData == null || douYinData.videoDataList.size() == 0) {
            if (isRefresh) {
                if (videoIndexAdapter.list == null || videoIndexAdapter.list.size() <= 0) {
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
            mDataList.addAll(douYinData.videoDataList);
            videoIndexAdapter.setList(mDataList);
            videoIndexAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected BaseListAdapter setUpAdapter() {
        /*
         * 解决界面闪屏问题，设置setHasStableIds =true
         * 在adapter中重写 getItemId 直接返回position
         * 不能直接用super.getItemId(position)
         */
        videoIndexAdapter.setHasStableIds(true);
        return videoIndexAdapter;
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getDouYinData(getActivity(), max_cursor);
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }
}
