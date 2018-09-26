package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.util.Log;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.databinding.FragmentBaseListBinding;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;

import java.util.ArrayList;
import java.util.List;

/**
 * 懒加载BaseListFragment
 * Created by zhouqiong on 2017/5/3.
 */

public abstract class BaseLazyListFragment<T> extends BaseFragment<FragmentBaseListBinding> implements PullRecycler.OnRecyclerRefreshListener {
    /**
     * 懒加载
     */
    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据
    /**
     * 初始化数据
     */
    public int action;
    private int page = 1;
    protected List<T> mDataList;
    public PullRecycler recycler;
    /**
     * isRefresh这个属性是添加缓存功能的时候用到的，我的缓存思路是：第一次进入界面走缓存，缓存为空的情况下
     * 网络请求数据，然后缓存起来，这个时候我需要一个属性判断是否需要刷新，之前在做刷新逻辑的时候没有将是否
     * 需要刷新暴露出来，让开发者自己选，这是一个小小的失误，（这里用isRefresh默认不刷新，除非用户调用了刷新一行才会刷新）
     */
    public boolean isRefresh = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void initData(Bundle bundle) {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isInitView = true;
        lazyLoadData();
        setUpPresenter();
    }

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

    private void initView() {
        if (mBinding != null) {
            recycler = mBinding.get().listView;
            recycler.setRefreshing();
            recycler.setOnRefreshListener(this);
            recycler.setLayoutManager(getLayoutManager());
            recycler.setAdapter(setUpAdapter());
        }
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

    private void lazyLoadData() {
        if (isFirstLoad) {
            Log.e("第一次加载 ", " isInitView  " + isInitView + "  isVisible  " + isVisible + "   " + this.getClass().getSimpleName());
        } else {
            Log.e("不是第一次加载", " isInitView  " + isInitView + "  isVisible  " + isVisible + "   " + this.getClass().getSimpleName());
        }
        if (!isFirstLoad || !isVisible || !isInitView) {
            Log.e("不加载" + "   ", this.getClass().getSimpleName());
            return;
        }

        Log.e("完成数据第一次加载" + "   ", this.getClass().getSimpleName());
        initView();
        isFirstLoad = false;
    }
}
