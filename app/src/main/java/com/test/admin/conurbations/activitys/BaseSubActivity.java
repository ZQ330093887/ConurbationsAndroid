package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.databinding.ActivityBaseSubBinding;
import com.test.admin.conurbations.di.component.ActivityComponent;
import com.test.admin.conurbations.di.component.DaggerActivityComponent;
import com.test.admin.conurbations.di.module.ActivityModule;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.presenter.BasePresenter;
import com.test.admin.conurbations.utils.RecyclerUtils;
import com.test.admin.conurbations.utils.SaveBitmapUtils;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.SolidApplication;
import com.test.admin.conurbations.widget.statuslayoutmanage.OnStatusChildClickListener;
import com.test.admin.conurbations.widget.statuslayoutmanage.StatusLayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2017/5/3.
 */

public abstract class BaseSubActivity<T, P extends BasePresenter> extends BaseActivity<ActivityBaseSubBinding>
        implements BaseViewImpl {
    /**
     * 初始化数据
     */
    private int page = 1;
    protected List<T> mDataList;
    private View mHeaderView;

    public StatusLayoutManager mStatusManager;

    public boolean isRefresh = false;

    @Inject
    protected P mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_sub;
    }

    @Override
    protected void initData(Bundle bundle) {

        initView();
        initRecycler();
        mPresenter.attachView(this);
        loadingData();
    }

    private void initRecycler() {
        mStatusManager = new StatusLayoutManager.Builder(mBinding.refreshLayout)
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
    }

    private void setupRecyclerView() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        if (mBinding != null) {
            RecyclerUtils.initLinearLayoutVertical(mBinding.listView);
            if (getLayoutManager() != null) {
                mBinding.listView.setLayoutManager(getLayoutManager().getLayoutManager());
            }
            if (setUpAdapter() != null) {
                mBinding.listView.setAdapter(setUpAdapter());
            }
        }
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setRefreshHeader(new ClassicsHeader(getBaseActivity()));
        mBinding.refreshLayout.setRefreshFooter(new ClassicsFooter(getBaseActivity()));

        mBinding.refreshLayout.setEnableLoadMore(true);
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            isRefresh = true;
            refreshList(page);
        });

        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            isRefresh = false;
            refreshList(page);
        });
    }

    protected abstract ILayoutManager getLayoutManager();

    protected abstract void refreshList(int page);

    protected abstract void initView();

    protected abstract void loadingData();

    protected abstract BaseListAdapter setUpAdapter();


    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(SolidApplication.getInstance().getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    public void finishRefreshAndLoadMore() {
        mBinding.refreshLayout.finishRefresh();
        mBinding.refreshLayout.finishLoadMore();
    }

    public void setRefreshLayoutEnableIsFalse() {
        mBinding.refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mBinding.refreshLayout.setEnableHeaderTranslationContent(false);
        mBinding.refreshLayout.setEnableFooterTranslationContent(false);

        mBinding.refreshLayout.setEnableLoadMore(false);
        mBinding.refreshLayout.setEnableRefresh(false);
    }

    public void setRefreshLayoutEnableRefresh(Boolean isRefresh) {

        mBinding.refreshLayout.setEnableHeaderTranslationContent(isRefresh);
        mBinding.refreshLayout.setEnableRefresh(isRefresh);
    }

    public void setRefreshLayoutEnableLoadMore(Boolean isRefresh) {
        mBinding.refreshLayout.setEnableLoadMoreWhenContentNotFull(isRefresh);
        mBinding.refreshLayout.setEnableLoadMore(isRefresh);

    }

    @Override
    public void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /*******************recycleView设置header默认的写了一个放这里，可自定义********************/
    public View getRecycleHeaderView(NewsList playlist) {
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.activity_online_header, null);
        showHeaderInfo(mHeaderView, playlist);
        return mHeaderView;
    }

    private void showHeaderInfo(View mHeaderView, NewsList playlist) {
        if (playlist != null) {
            ImageView mIvCover = mHeaderView.findViewById(R.id.iv_cover);
            TextView mTvTitle = mHeaderView.findViewById(R.id.tv_title);
            TextView mTvDesc = mHeaderView.findViewById(R.id.tv_comment);
            ImageView mIvBackground = mHeaderView.findViewById(R.id.coverBgIv);

            SaveBitmapUtils.loadBitmap(this, playlist.coverUrl, bitmap -> {
                mIvCover.setImageBitmap(bitmap);
                mIvBackground.setImageBitmap(bitmap);
                mIvBackground.setImageDrawable(SaveBitmapUtils.createBlurredImageFromBitmap(bitmap));
            });

            mTvTitle.setText(playlist.name);
            mTvDesc.setText(playlist.des);
        }
    }
}
