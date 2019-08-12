package com.test.admin.conurbations.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IVideoInfoView;
import com.test.admin.conurbations.activitys.VideoDetailActivity;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.VideoIndexAdapter;
import com.test.admin.conurbations.model.entity.DouyinVideoListData;
import com.test.admin.conurbations.model.entity.LeVideoData;
import com.test.admin.conurbations.model.entity.RefreshEvent;
import com.test.admin.conurbations.presenter.VideoIndexPresenter;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.utils.WeakDataHolder;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhouqiong on 2019/4/2.
 */

public class VideoIndexFragment extends BaseSubFragment<LeVideoData, VideoIndexPresenter>
        implements IVideoInfoView {
    private long max_cursor = 0;
    private int index = 0;
    private Disposable subscribe;

    @Inject
    VideoIndexAdapter videoIndexAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
        subscribe = RxBus.getDefault().toObservable(RefreshEvent.class).subscribe(event -> {
            index = event.getPosition();

            max_cursor = event.getMaxCursor();
            mBinding.get().listView.scrollToPosition(index);
        });
    }

    @Override
    protected void loadingData() {
        if (mPresenter != null) {
            mPresenter.getDouYinData(getActivity(), max_cursor);
        }
    }

    @Override
    public void setVideoDouYinData(DouyinVideoListData douYinData) {
        mStatusManager.showSuccessLayout();
        if (douYinData == null || douYinData.videoDataList.size() == 0) {
            max_cursor = 0;
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
            max_cursor = douYinData.maxCursor;
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
        videoIndexAdapter.setOnItemClickListener(this::startVideoDetail);
        return videoIndexAdapter;
    }


    private void startVideoDetail(Object item) {
        int position = -1;
        if (videoIndexAdapter == null || videoIndexAdapter.list == null || videoIndexAdapter.list.size() == 0)
            return;
        LeVideoData data = (LeVideoData) item;
        if (videoIndexAdapter.list.contains(data)) {
            position = videoIndexAdapter.list.indexOf(data);
        }
        Intent intent = new Intent(getContext(), VideoDetailActivity.class);
        WeakDataHolder.getInstance().saveData(VideoDetailActivity.VIDEO_DATA, videoIndexAdapter.list);
        intent.putExtra("max_cursor", max_cursor);
        intent.putExtra("position", position);
        getContext().startActivity(intent);
    }

    @Override
    protected void refreshList(int page) {
        if (page == 1) {
            max_cursor = 0;
        }
        if (mPresenter != null) {
            mPresenter.getDouYinData(getActivity(), max_cursor);
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
            subscribe = null;
        }
    }
}
