package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.activitys.ITodayNewsList;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.TodayListAdapter;
import com.test.admin.conurbations.data.entity.Moment;
import com.test.admin.conurbations.data.response.GankItem;
import com.test.admin.conurbations.presenter.TodayDataPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

import java.util.Calendar;
import java.util.List;

public class TodayNewsFragment extends BaseListFragment<List<GankItem>> implements ITodayNewsList {

    private Moment.Range range;
    protected TodayListAdapter adapter;
    protected TodayDataPresenter presenter;

    public void setRange(Moment.Range range) {
        this.range = range;
    }

    Calendar calendar = Calendar.getInstance();
    int Year = calendar.get(Calendar.YEAR);
    int Month = calendar.get(Calendar.MONTH) + 1;
    int Day = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    public void setTodayData(List<GankItem> items) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (items.size() == 0) {
            presenter.getTodayData(Year, Month, Day - 1);
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(false);
            mDataList.add(items);
            adapter.setList(mDataList);
            adapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        return viewRoot;
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        adapter = new TodayListAdapter(getActivity());
        return adapter;
    }

    @Override
    protected void setUpPresenter() {
        presenter = new TodayDataPresenter(this);
    }

    @Override
    protected void refreshList(int page) {
        presenter.getTodayData(Year, Month, Day);
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }
}
