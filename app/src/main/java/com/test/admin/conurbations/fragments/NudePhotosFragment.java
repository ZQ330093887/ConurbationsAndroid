package com.test.admin.conurbations.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.test.admin.conurbations.activitys.INudePhotosView;
import com.test.admin.conurbations.activitys.ShowGoodsImageActivity;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.NudeDetailAdapter;
import com.test.admin.conurbations.model.entity.MenuModel;
import com.test.admin.conurbations.model.entity.PageModel;
import com.test.admin.conurbations.presenter.NudeDetailPresenter;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.EventType;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class NudePhotosFragment extends BaseSubFragment<PageModel.ItemModel, NudeDetailPresenter> implements INudePhotosView {

    private String url;
    private PageModel pageModel;
    private ArrayList<String> imageList;
    private Disposable subscribe;

    public String nextPage = "";

    public void setRange(String url) {
        this.url = url;
    }

    @Inject
    NudeDetailAdapter nudeDetailAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);

        initListener();
    }

    private void initListener() {
        subscribe = RxBus.getDefault().toObservable(Event.class).subscribe(event -> {
            if (event.eventType.equals(EventType.IMAGE_POSITION)) {
                PageModel pageM = (PageModel) event.body;
                mBinding.get().listView.scrollToPosition(pageM.index);

                if (!TextUtils.isEmpty(pageM.nextPage) && !nextPage.equals(pageM.nextPage)) {
                    Log.d("1:>>", nextPage);
                    Log.d("2:>>", pageM.nextPage);
                    nextPage = pageM.nextPage;
                    mPresenter.getNudeDetail(pageM.nextPage, 2);
                }
            }
        });
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
        if (nudeDetailAdapter != null) {
            nudeDetailAdapter.setOnItemListener(onItemClickListener);
        }
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
        if (getActivity() == null || mStatusManager == null) return;
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

    /**
     * item点击事件
     */
    NudeDetailAdapter.OnItemClickListener onItemClickListener = new NudeDetailAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, PageModel.ItemModel itemModel) {
            if (imageList == null) {
                imageList = new ArrayList<>();
            }
            imageList.clear();
            int index = 0;

            if (nudeDetailAdapter.list != null && nudeDetailAdapter.list.size() > 0) {
                for (int i = 0; i < nudeDetailAdapter.list.size(); i++) {
                    if (nudeDetailAdapter.list.get(i).equals(itemModel)) {
                        index = i;
                    }
                    imageList.add(nudeDetailAdapter.list.get(i).imgUrl);
                }
            }

            Intent intent = new Intent(view.getContext(), ShowGoodsImageActivity.class);
            intent.putStringArrayListExtra(ShowGoodsImageActivity.IMAGE_URL, imageList);
            intent.putExtra(ShowGoodsImageActivity.POSITION, index);
            intent.putExtra(ShowGoodsImageActivity.PAGEDATA, pageModel);
            view.getContext().startActivity(intent);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
            subscribe = null;
        }
    }
}
