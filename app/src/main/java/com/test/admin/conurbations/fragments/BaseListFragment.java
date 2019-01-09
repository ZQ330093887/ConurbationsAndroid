package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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
 * Created by zhouqiong on 2017/1/3.
 * 这个类本应该跟BaseLazyListFragment是一个类，但是在处理逻辑的时候发现一些问题，所以重新抽出一个类专门
 * 处理懒加载，不需要懒加载这个功能则依然使用或者类
 */

public abstract class BaseListFragment<T, P extends BasePresenter>
        extends BaseFragment<FragmentSubListBinding> implements BaseViewImpl {

    /**
     * 初始化数据
     */
    public int page = 1;
    protected List<T> mDataList;
    public boolean isRefresh = false;//不是刷新就是加载更多
    public StatusLayoutManager mStatusManager;

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
        return R.layout.fragment_sub_list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mStatusManager = new StatusLayoutManager.Builder(mBinding.get().refreshLayout)
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
            mBinding.get().listView.setAdapter(setUpAdapter());
        }
    }


    public RecyclerView getRecyclerView() {
        return mBinding.get().listView;
    }

    protected abstract void loadingData();

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

    public void finishRefreshAndLoadMore() {
        mBinding.get().refreshLayout.finishRefresh();
        mBinding.get().refreshLayout.finishLoadMore();
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
