package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;

import com.test.admin.conurbations.activitys.INudePhotosView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.NudeDetailAdapter;
import com.test.admin.conurbations.model.entity.MenuModel;
import com.test.admin.conurbations.model.entity.PageModel;
import com.test.admin.conurbations.presenter.NudeDetailPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class NudePhotosFragment extends BaseSubFragment<PageModel.ItemModel, NudeDetailPresenter> implements INudePhotosView {

    private String url;
    private PageModel pageModel;

    public void setRange(String url) {
        this.url = url;
    }

    @Inject
    NudeDetailAdapter nudeDetailAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }


    @Override
    protected void loadingData() {
        mPresenter.getCacheData(url);
    }

    @Override
    public void setCacheNudePhotos(PageModel pageModel) {
        mStatusManager.showSuccessLayout();
        if (pageModel != null && pageModel.itemList != null && pageModel.itemList.size() > 0) {
            mDataList.clear();
            mDataList.addAll(pageModel.itemList);
            nudeDetailAdapter.setList(mDataList);
            nudeDetailAdapter.notifyDataSetChanged();
            this.pageModel = pageModel;
        } else {
            mStatusManager.showLoadingLayout();
            isRefresh = true;
            refreshList(1);
        }
    }


    @Override
    protected BaseListAdapter setUpAdapter() {
        return nudeDetailAdapter;
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        if (mPresenter != null) {
            if (page == 1) {
                mPresenter.getNudeDetail(url, page);
            } else {
                if (pageModel != null && !TextUtils.isEmpty(pageModel.nextPage)) {
                    mPresenter.getNudeDetail(pageModel.nextPage, page);
                }
            }
        }
    }

    @Override
    public void setNodePhotoData(List<MenuModel> list) {
        //该接口这里不做处理
    }

    @Override
    public void setNodeDetailData(PageModel pageModel) {
        getActivity().runOnUiThread(() -> {
            mStatusManager.showSuccessLayout();
            if (pageModel == null || pageModel.itemList == null || pageModel.itemList.size() <= 0) {
                if (isRefresh) {
                    if (nudeDetailAdapter.list == null || nudeDetailAdapter.list.size() <= 0) {
                        mStatusManager.showEmptyLayout();
                    }
                } else {
                    if (page > 1) {
                        mBinding.get().refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                }
            } else {
                this.pageModel = pageModel;
                if (isRefresh) {
                    mDataList.clear();
                    mBinding.get().refreshLayout.finishRefresh(true);
                } else {
                    mBinding.get().refreshLayout.finishLoadMore(true);
                }
                mDataList.addAll(pageModel.itemList);
                nudeDetailAdapter.setList(mDataList);
                nudeDetailAdapter.notifyDataSetChanged();
            }
        });
    }
}
