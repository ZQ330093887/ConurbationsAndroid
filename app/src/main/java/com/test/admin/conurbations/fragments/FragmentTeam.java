package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.activitys.INewListView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.FragmentNewsRecyclerViewAdapter;
import com.test.admin.conurbations.model.NewsList;
import com.test.admin.conurbations.presenter.FragmentTeamPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

/**
 * Created by zhouqiong on 2015/9/23.
 */
public class FragmentTeam extends BaseListFragment implements INewListView {

    protected FragmentNewsRecyclerViewAdapter newsRecyclerViewAdapter;
    protected FragmentTeamPresenter fragmentTeamPresenter;
    protected String ordDate;
    private String range;

    public void setRange(String range) {
        this.range = range;
    }


    @Override
    public void setNewListData(NewsList newListData) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (newListData.stories == null || newListData.stories.size() == 0) {
            recycler.enableLoadMore(false);
        } else {
            ordDate = newListData.getDate();
            recycler.enableLoadMore(true);
            mDataList.addAll(newListData.stories);
            newsRecyclerViewAdapter.setList(mDataList);
            newsRecyclerViewAdapter.setDate(ordDate);
            newsRecyclerViewAdapter.notifyDataSetChanged();
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
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        fragmentTeamPresenter.getNewListData(page,ordDate);
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return newsRecyclerViewAdapter = new FragmentNewsRecyclerViewAdapter(getActivity());
    }

    @Override
    protected void setUpPresenter() {
        fragmentTeamPresenter = new FragmentTeamPresenter(this);
    }
}
