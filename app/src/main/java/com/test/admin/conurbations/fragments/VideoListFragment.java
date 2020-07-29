package com.test.admin.conurbations.fragments;

import android.os.Bundle;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.lNewsListView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.VideoListAdapter;
import com.test.admin.conurbations.model.entity.TTNews;
import com.test.admin.conurbations.presenter.NewsListPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.util.List;

import javax.inject.Inject;

/**
 * 头条视频数据
 * Created by zhouqiong on 2019/7/4.
 */

public class VideoListFragment extends BaseSubFragment<TTNews, NewsListPresenter>
        implements lNewsListView {

    String mChannelCode = "";

    public void setRange(String range) {
        this.mChannelCode = range;
    }

    @Inject
    VideoListAdapter videoIndexAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void loadingData() {
        isRefresh = true;
        if (mPresenter != null) {
            mPresenter.getNewsList(mChannelCode);
        }
    }

    @Override
    public void onGetNewsListSuccess(List<TTNews> newList) {
        mStatusManager.showSuccessLayout();

        if (newList == null || newList.size() == 0) {
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
            mDataList.addAll(newList);
            videoIndexAdapter.setList(mDataList);
            videoIndexAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return videoIndexAdapter;
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getNewsList(mChannelCode);
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }
}
