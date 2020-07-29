package com.test.admin.conurbations.fragments;

import android.os.Bundle;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.INewInformationView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.NetPlayListAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.presenter.NewsInfoListPresenter;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.util.List;

import javax.inject.Inject;

/**
 * 排行榜
 * Created by zhouqiong on 2016/9/23.
 */
public class NetPlayListFragment extends BaseSubFragment<NewsList, NewsInfoListPresenter> implements INewInformationView {

    protected String tabName;
    private int[] ids = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};

    public void setTable(String tabName, String type) {
        this.tabName = tabName;
        this.chartsType = type;
    }

    private String chartsType = Constants.BAIDU;

    @Override
    public void setNewInfoData(List<NewsList> result) {
        mStatusManager.showSuccessLayout();
        mDataList.addAll(result);
        mInformationListAdapter.setList(mDataList);
        mInformationListAdapter.notifyDataSetChanged();
    }

    @Inject
    NetPlayListAdapter mInformationListAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void loadingData() {
        mBinding.get().refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mBinding.get().refreshLayout.setEnableHeaderTranslationContent(false);
        mBinding.get().refreshLayout.setEnableLoadMore(false);
        mBinding.get().refreshLayout.setEnableRefresh(false);
        getPlayList();
    }


    @Override
    protected BaseListAdapter setUpAdapter() {
        return mInformationListAdapter;
    }

    @Override
    protected void refreshList(int page) {
        getPlayList();
    }

    private void getPlayList() {
        if (mPresenter != null) {
            switch (chartsType) {
                case Constants.BAIDU:
                    mPresenter.loadBaiDuPlaylist();
                    break;
                case Constants.QQ:
                    mPresenter.loadQQList();
                    break;
                case Constants.NETEASE:
                    mPresenter.loadTopList(ids, 3);
                    break;
            }
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }
}
