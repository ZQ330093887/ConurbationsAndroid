package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.IDownLoadView;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.SongAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.presenter.DownLoadPresenter;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.utils.download.TasksManagerModel;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class DownloadedFragment extends BaseSubFragment<Music, DownLoadPresenter> implements IDownLoadView {

    private Boolean isCache;

    private Disposable subscribe;

    public static DownloadedFragment newInstance(Boolean isCache) {
        Bundle args = new Bundle();
        args.putBoolean("is_cache", isCache);
        DownloadedFragment fragment = new DownloadedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    SongAdapter mAdapter;

    @Override
    protected void initData(Bundle bundle) {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void loadingData() {
        mBinding.get().refreshLayout.setEnableLoadMore(false);
        mBinding.get().refreshLayout.setEnableRefresh(false);
        getDownloadMusic();

        subscribe = RxBus.getDefault().toObservable(Event.class).subscribe(event -> {
            if (event.eventType.equals(Constants.DOWNLOAD_EVENT)) {
                mPresenter.loadDownloadMusic(isCache, getBaseActivity());
                mPresenter.loadDownloading(getBaseActivity());
            }
        });
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        mAdapter.setOnItemClickListener((music, view, position) -> {
            if (view.getId() == R.id.iv_more) {
                BottomDialogFragment.newInstance(music).show(getBaseActivity());
            } else {
                PlayManager.play(position, mDataList, Constants.PLAYLIST_DOWNLOAD_ID);
                mAdapter.notifyDataSetChanged();
            }
        });
        return mAdapter;
    }

    @Override
    protected void refreshList(int page) {
        getDownloadMusic();
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    private void getDownloadMusic() {
        if (getArguments() != null) {
            isCache = getArguments().getBoolean("is_cache");
        }
        if (mPresenter != null) {
            mPresenter.loadDownloadMusic(isCache, getBaseActivity());
        }
    }

    @Override
    public void showSongs(List<Music> musicList) {
        mStatusManager.showSuccessLayout();
        mDataList.clear();
        mDataList.addAll(musicList);
        mAdapter.setList(musicList);

        if (mDataList.size() == 0) {
            mStatusManager.showEmptyLayout();
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void showDownloadList(List<TasksManagerModel> modelList) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
            subscribe = null;
        }
    }
}
