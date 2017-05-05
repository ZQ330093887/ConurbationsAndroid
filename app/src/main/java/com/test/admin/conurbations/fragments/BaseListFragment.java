package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.util.Log;
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
 */

public abstract class BaseListFragment<T> extends BaseFragment implements PullRecycler.OnRecyclerRefreshListener {
    /**
     * 懒加载
     */
    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据
    /**
     * 初始化数据
     */
    public PullRecycler recycler;
    private int page = 1;
    protected List<T> mDataList;
    public int action;
    private View contentView;

    @Override
    public BaseFragment newInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle bundle) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_base_list, container, false);
        isInitView = true;
        lazyLoadData();
        setUpPresenter();
        return contentView;
    }

    private void initView() {
        recycler = (PullRecycler) contentView.findViewById(R.id.pullRecycler);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.e("isVisibleToUser " + isVisibleToUser + "   ", this.getClass().getSimpleName());
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();

        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void lazyLoadData() {

        if (!isFirstLoad || !isVisible || !isInitView) {
            Log.e("不加载" + "   ", this.getClass().getSimpleName());
            return;
        }
        initView();
        isFirstLoad = false;
    }
}
