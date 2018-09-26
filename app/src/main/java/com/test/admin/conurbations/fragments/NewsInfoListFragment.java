package com.test.admin.conurbations.fragments;

import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.INewInformationView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.NewsInfoListAdapter;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.presenter.NewsInfoListPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class NewsInfoListFragment extends BaseLazyListFragment implements INewInformationView {

    protected String mTabId;
    protected NewsInfoListAdapter mInformationListAdapter;
    protected NewsInfoListPresenter mNewsInfoListPresenter;

    public void setTable(String tabId) {
        this.mTabId = tabId;
    }

    @Override
    public void setNewInfoData(NewsList informationBean) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (informationBean == null || informationBean.stories.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(true);
            mDataList.addAll(informationBean.stories);
            mInformationListAdapter.setList(mDataList);
            mInformationListAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        mInformationListAdapter = new NewsInfoListAdapter();
        return mInformationListAdapter;
    }

    @Override
    protected void setUpPresenter() {
        mNewsInfoListPresenter = new NewsInfoListPresenter(this);
    }

    @Override
    protected void refreshList(int page) {
        if (mNewsInfoListPresenter != null) {
            mNewsInfoListPresenter.getNewsInfoData(mTabId, page, isRefresh);
            isRefresh = true;
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public void detachView() {
        if (mNewsInfoListPresenter != null) {
            mNewsInfoListPresenter.detachView();
        }
    }
}
