package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IGankDayView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.GankDayAdapter;
import com.test.admin.conurbations.model.response.GankItem;
import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.presenter.GankDayPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class GankDayFragment extends BaseLazyListFragment<List<GankItem>, GankDayPresenter> implements IGankDayView {

    private Moment.Range range;

    public void setRange(Moment.Range range) {
        this.range = range;
    }

    Calendar calendar = Calendar.getInstance();
    int Year = calendar.get(Calendar.YEAR);
    int Month = calendar.get(Calendar.MONTH) + 1;
    int Day = calendar.get(Calendar.DAY_OF_MONTH);

    @Inject
    GankDayAdapter mGankDayAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }

    @Override
    public void setGankDayData(List<GankItem> items) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (items.size() == 0) {
            mPresenter.getGankDayData(Year, Month, Day--, true);
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(false);
            mDataList.add(items);
            mGankDayAdapter.setList(mDataList);
            mGankDayAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return mGankDayAdapter;
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            mPresenter.getGankDayData(Year, Month, Day, isRefresh);
            isRefresh = true;
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }
}
