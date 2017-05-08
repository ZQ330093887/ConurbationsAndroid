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

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class GankDayFragment extends BaseLazyListFragment<List<GankItem>> implements IGankDayView {

    private Moment.Range range;
    protected GankDayAdapter mGankDayAdapter;
    protected GankDayPresenter mGankDayPresenter;

    public void setRange(Moment.Range range) {
        this.range = range;
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    Calendar calendar = Calendar.getInstance();
    int Year = calendar.get(Calendar.YEAR);
    int Month = calendar.get(Calendar.MONTH) + 1;
    int Day = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    public void setGankDayData(List<GankItem> items) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (items.size() == 0) {
            mGankDayPresenter.getGankDayData(Year, Month, Day--, true);
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
        mGankDayAdapter = new GankDayAdapter(getActivity());
        return mGankDayAdapter;
    }

    @Override
    protected void setUpPresenter() {
        mGankDayPresenter = new GankDayPresenter(this);
    }

    @Override
    protected void refreshList(int page) {
        if (mGankDayPresenter != null) {
            mGankDayPresenter.getGankDayData(Year, Month, Day, isRefresh);
            isRefresh = true;
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }
}
