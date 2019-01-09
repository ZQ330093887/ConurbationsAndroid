package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.activitys.IDownLoadView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.TaskItemAdapter;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.presenter.DownLoadPresenter;
import com.test.admin.conurbations.utils.download.TasksManager;
import com.test.admin.conurbations.utils.download.TasksManagerModel;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class DownloadManagerFragment extends BaseSubFragment<TasksManagerModel, DownLoadPresenter>
        implements IDownLoadView {
    private TaskItemAdapter mAdapter;
    private DownLoadPresenter mPresenter;

    public static DownloadManagerFragment newInstance() {
        Bundle args = new Bundle();
        DownloadManagerFragment fragment = new DownloadManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
        TasksManager.onCreate(new WeakReference(this));
        mAdapter = new TaskItemAdapter(getActivity(), mDataList);
        getRecyclerView().setAdapter(mAdapter);
    }

    @Override
    protected void loadingData() {
        mBinding.get().refreshLayout.setEnableLoadMore(false);
        mBinding.get().refreshLayout.setEnableRefresh(false);
        loadData();
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        loadData();
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return null;
    }


    private void loadData() {
        if (mPresenter == null) {
            mPresenter = new DownLoadPresenter();
            mPresenter.attachView(this);
        }
        mPresenter.loadDownloading(getBaseActivity());
    }

    @Override
    public void showSongs(List<Music> musicList) {
    }

    @Override
    public void showDownloadList(List<TasksManagerModel> modelList) {
        mDataList.clear();
        mDataList.addAll(modelList);
        mAdapter.notifyDataSetChanged();

        if (mDataList == null || mDataList.size() <= 0) {
            mStatusManager.showEmptyLayout();
        }
    }


    public void postNotifyDataChanged() {
        getBaseActivity().runOnUiThread(() -> {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void detachView() {
        mPresenter.detachView();
    }
}
