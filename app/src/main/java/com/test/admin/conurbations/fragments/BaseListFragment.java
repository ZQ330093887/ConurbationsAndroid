package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouqiong on 2017/1/3.
 * 这个类本应该跟BaseLazyListFragment是一个类，但是在处理逻辑的时候发现一些问题，所以重新抽出一个类专门
 * 处理懒加载，不需要懒加载这个功能则依然使用或者类
 */

public abstract class BaseListFragment<T> extends BaseFragment implements PullRecycler.OnRecyclerRefreshListener {

    public PullRecycler recycler;
    private int page = 1;
    protected List<T> mDataList;
    public int action;

    @Override
    protected void initData(Bundle bundle) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_base_list, container, false);
        initView(contentView);
        setUpPresenter();
        initData(savedInstanceState);
        return contentView;
    }

    private void initView(View view) {
        recycler = (PullRecycler) view.findViewById(R.id.pullRecycler);
        recycler.setRefreshing();
        recycler.setOnRefreshListener(this);
        recycler.setLayoutManager(getLayoutManager());
        recycler.setAdapter(setUpAdapter());
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

    public PullRecycler getRecyclerView() {
        return recycler;
    }

    protected abstract ILayoutManager getLayoutManager();

    protected abstract void refreshList(int page);

    protected abstract BaseListAdapter setUpAdapter();

    protected abstract void setUpPresenter();


}
