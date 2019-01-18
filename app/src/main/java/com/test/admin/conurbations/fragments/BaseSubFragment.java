package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.BaseViewImpl;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.databinding.FragmentSubListBinding;
import com.test.admin.conurbations.di.component.DaggerFragmentComponent;
import com.test.admin.conurbations.di.component.FragmentComponent;
import com.test.admin.conurbations.di.module.FragmentModule;
import com.test.admin.conurbations.presenter.BasePresenter;
import com.test.admin.conurbations.utils.RecyclerUtils;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.SolidApplication;
import com.test.admin.conurbations.widget.statuslayoutmanage.OnStatusChildClickListener;
import com.test.admin.conurbations.widget.statuslayoutmanage.StatusLayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 懒加载BaseListFragment
 * Created by zhouqiong on 2017/5/3.
 */

public abstract class BaseSubFragment<T, P extends BasePresenter>
        extends BaseFragment<FragmentSubListBinding> implements BaseViewImpl {
    /**
     * 懒加载
     */
    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据
    /**
     * 初始化数据
     */
    public int page = 1;
    protected List<T> mDataList;
    public StatusLayoutManager mStatusManager;
    /**
     * isRefresh这个属性是添加缓存功能的时候用到的，我的缓存思路是：第一次进入界面走缓存，缓存为空的情况下
     * 网络请求数据，然后缓存起来，这个时候我需要一个属性判断是否需要刷新，之前在做刷新逻辑的时候没有将是否
     * 需要刷新暴露出来，让开发者自己选，这是一个小小的失误，（这里用isRefresh默认不刷新，除非用户调用了刷新一行才会刷新）
     */
    public boolean isRefresh = false;//不是刷新就是加载更多

    @Inject
    protected P mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sub_list;
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isInitView = true;
        lazyLoadData();
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

        mStatusManager = new StatusLayoutManager.Builder(mBinding.get().refreshLayout)
                .setDefaultEmptyClickViewVisible(false)
                .setOnStatusChildClickListener(new OnStatusChildClickListener() {
                    @Override
                    public void onEmptyChildClick(View view) {
                    }

                    @Override
                    public void onErrorChildClick(View view) {
                        mStatusManager.showLoadingLayout();
                        isRefresh = true;
                        page = 0;
                        refreshList(page);
                    }

                    @Override
                    public void onCustomerChildClick(View view) {
                    }
                }).build();
        mStatusManager.showLoadingLayout();

        setupRecyclerView();
        initRefreshLayout();

        loadingData();
    }

    private void initRefreshLayout() {
        mBinding.get().refreshLayout.setRefreshHeader(new ClassicsHeader(getBaseActivity()));
        mBinding.get().refreshLayout.setRefreshFooter(new ClassicsFooter(getBaseActivity()));

        mBinding.get().refreshLayout.setEnableLoadMore(true);
        mBinding.get().refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            isRefresh = true;
            refreshList(page);
        });

        mBinding.get().refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            isRefresh = false;
            refreshList(page);
        });
    }

    private void setupRecyclerView() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        if (mBinding != null) {
            RecyclerUtils.initLinearLayoutVertical(mBinding.get().listView);
            if (getLayoutManager() != null) {
                mBinding.get().listView.setLayoutManager(getLayoutManager().getLayoutManager());
            }
            if (setUpAdapter() != null) {
                mBinding.get().listView.setAdapter(setUpAdapter());
            }
        }
    }


    public RecyclerView getRecyclerView() {
        return mBinding.get().listView;
    }

    protected abstract void loadingData();

    protected abstract ILayoutManager getLayoutManager();

    protected abstract void refreshList(int page);


    protected abstract BaseListAdapter setUpAdapter();

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

    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(SolidApplication.getInstance().getAppComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }

    protected FragmentModule getFragmentModule() {
        return new FragmentModule(this);
    }


    public void finishRefreshAndLoadMore() {
        mBinding.get().refreshLayout.finishRefresh();
        mBinding.get().refreshLayout.finishLoadMore();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void showError(String message) {

        if (mDataList != null && mDataList.size() > 0) {
            ToastUtils.getInstance().showToast(message);
        } else {
            mStatusManager.showErrorLayout();
        }
    }

    @Override
    public void showFinishState() {
        //请求完毕，不管成功失败
        finishRefreshAndLoadMore();
    }

    @Override
    public void detachView() {

    }
}
