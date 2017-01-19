package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.activitys.IDayAndDayPrettyPictureView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.DayAndDayPrettyPictureAdapter;
import com.test.admin.conurbations.data.entity.Moment;
import com.test.admin.conurbations.model.NetImage360;
import com.test.admin.conurbations.presenter.DayAndDayPrettyPicturesPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

public class DayAndDayPrettyPicturesFragment extends BaseListFragment implements IDayAndDayPrettyPictureView {

    private Moment.SGImgType range;

    public void setRange(Moment.SGImgType range) {
        this.range = range;
    }

    protected DayAndDayPrettyPicturesPresenter dayAndDayPrettyPicturesPresenter;
    protected DayAndDayPrettyPictureAdapter dayAndDayPrettyPictureAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        return viewRoot;
    }

    @Override
    public void setDayPrettyPicture(NetImage360 netImage) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (netImage.data == null && netImage.data.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            recycler.enableLoadMore(false);
            mDataList.addAll(netImage.data);
            dayAndDayPrettyPictureAdapter.setList(mDataList);
            dayAndDayPrettyPictureAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyGridLayoutManager(getContext(),2,  GridLayoutManager.VERTICAL, false);
    }

    @Override
    protected void refreshList(int page) {
        dayAndDayPrettyPicturesPresenter.getWelfareData();
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        dayAndDayPrettyPictureAdapter = new DayAndDayPrettyPictureAdapter();
        return dayAndDayPrettyPictureAdapter;
    }

    @Override
    protected void setUpPresenter() {
        dayAndDayPrettyPicturesPresenter = new DayAndDayPrettyPicturesPresenter(this);
    }
}
