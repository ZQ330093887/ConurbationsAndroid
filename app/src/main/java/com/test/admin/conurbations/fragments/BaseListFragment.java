package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.data.response.GanHuoDataBean;
import com.test.admin.conurbations.widget.DividerItemDecoration;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouqiong on 2017/1/3.
 */

public abstract class BaseListFragment extends Fragment implements PullRecycler.OnRecyclerRefreshListener {

    PullRecycler recycler;
    private int page = 1;
    protected List<GanHuoDataBean> mDataList;
    public int action;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_base_list, container, false);
        initView(contentView);
        setUpPresenter();
        return contentView;
    }

    private void initView(View view) {
        recycler = (PullRecycler) view.findViewById(R.id.pullRecycler);
        recycler.setRefreshing();
        recycler.setOnRefreshListener(this);
        recycler.setLayoutManager(getLayoutManager());
        recycler.addItemDecoration(getItemDecoration());
        recycler.setAdapter(setUpAdapter());
    }

    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext(), R.drawable.list_divider);
    }

    @Override
    public void onRefresh(int action) {
        this.action = action;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }
        refreshList(page++);
    }


    protected abstract void refreshList(int page);

    protected abstract BaseListAdapter setUpAdapter();

    protected abstract void setUpPresenter();
}
