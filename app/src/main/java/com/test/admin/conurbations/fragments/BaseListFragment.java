package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.databinding.FragmentBaseListBinding;
import com.test.admin.conurbations.di.component.DaggerFragmentComponent;
import com.test.admin.conurbations.di.component.FragmentComponent;
import com.test.admin.conurbations.di.module.FragmentModule;
import com.test.admin.conurbations.presenter.BasePresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.PullRecycler;
import com.test.admin.conurbations.widget.SolidApplication;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2017/1/3.
 * 这个类本应该跟BaseLazyListFragment是一个类，但是在处理逻辑的时候发现一些问题，所以重新抽出一个类专门
 * 处理懒加载，不需要懒加载这个功能则依然使用或者类
 */

public abstract class BaseListFragment<T, P extends BasePresenter> extends BaseFragment<FragmentBaseListBinding> implements PullRecycler.OnRecyclerRefreshListener {

    public PullRecycler recycler;
    private int page = 1;
    protected List<T> mDataList;
    public int action;

    @Inject
    protected P mPresenter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
    }


    @Override
    protected void initData(Bundle bundle) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
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


    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(SolidApplication.getInstance().getAppComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }

    protected FragmentModule getFragmentModule() {
        return new FragmentModule(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void detachView() {

    }
}
