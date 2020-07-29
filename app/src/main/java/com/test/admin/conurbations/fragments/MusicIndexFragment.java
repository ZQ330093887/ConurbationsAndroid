package com.test.admin.conurbations.fragments;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.IMuMusicContractView;
import com.test.admin.conurbations.adapter.MyMusicAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.FragmentMusicIndexBinding;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.model.user.UserStatus;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.presenter.MyMusicPresenter;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.download.PlaylistLoader;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhouqiong on 2016/9/23.
 */

public class MusicIndexFragment extends BaseFragment<FragmentMusicIndexBinding> implements IMuMusicContractView {
    private MyMusicPresenter mPresenter;
    private MyMusicAdapter mAdapter;
    private Disposable mSubscribe;

    public static MusicIndexFragment newInstance() {
        Bundle args = new Bundle();
        MusicIndexFragment fragment = new MusicIndexFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music_index;
    }

    @Override
    protected void initData(Bundle bundle) {
        initView();
        loadData();
        listener();
    }

    private void initView() {
        mPresenter = new MyMusicPresenter(this, getBaseActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(false);
        mBinding.get().playlistRcv.setLayoutManager(linearLayoutManager);
        mBinding.get().playlistRcv.setNestedScrollingEnabled(false);
        mAdapter = new MyMusicAdapter(getBaseActivity());
        mBinding.get().playlistRcv.setAdapter(mAdapter);

    }

    private void loadData() {
        mPresenter.loadSongs();
        if (UserStatus.getLoginStatus() && UserStatus.getTokenStatus()) {
            mPresenter.loadPlaylist();
        }


        mSubscribe = RxBus.getDefault().toObservable(Event.class).subscribe(event -> {
            if (event.eventType.equals(Constants.META_CHANGED_EVENT)) {
                mPresenter.updateHistory();
            } else if (event.eventType.equals(Constants.PLAYLIST_LOCAL_ID)) {
                mPresenter.loadSongs();
            } else if (event.eventType.equals(Constants.PLAYLIST_CUSTOM_ID)) {
                mPresenter.loadPlaylist();
            } else if (event.eventType.equals(Constants.PLAYLIST_LOVE_ID)) {
                mPresenter.updateFavorite();
            } else if (event.eventType.equals(Constants.PLAYLIST_HISTORY_ID)) {
                mPresenter.updateHistory();
            } else if (event.eventType.equals(Constants.PLAYLIST_DOWNLOAD_ID)) {
                mPresenter.updateDownload();
            } else if (event.eventType.equals(String.valueOf(Constants.PLAYLIST_UPDATE))) {
                mPresenter.loadPlaylist();
            } else if (event.eventType.equals(String.valueOf(Constants.PLAYLIST_RENAME))) {
                if (mAdapter != null && mAdapter.getList() != null) {
                    NewsList playlist = (NewsList) event.body;
                    List<NewsList> playlists = mAdapter.getList();
                    for (int i = 0; i < playlists.size(); i++) {
                        if (playlists.get(i).pid.equals(playlist.pid)) {
                            playlists.get(i).name = playlist.name;
                            mAdapter.notifyItemChanged(i);
                            return;
                        }
                    }
                }
            } else if (event.eventType.equals(String.valueOf(Constants.PLAYLIST_ADD))) {
                mPresenter.loadPlaylist();
            } else if (event.eventType.equals(String.valueOf(Constants.PLAYLIST_DELETE))) {
                NewsList playlist = (NewsList) event.body;
                List<NewsList> playlists = mAdapter.getList();
                for (int i = 0; i < playlists.size(); i++) {
                    if (playlists.get(i).pid.equals(playlist.pid)) {
                        mAdapter.getList().remove(i);
                        mAdapter.notifyItemRemoved(i);
                        return;
                    }
                }
            } else if (event.eventType.equals(Constants.DOWNLOAD_EVENT)) {
                mPresenter.updateDownload();
            }
        });
    }

    @Override
    public void showSongs(List<Music> songList) {

        mBinding.get().localView.setSongsNum(songList.size(), 0);
        mBinding.get().localView.setOnItemClickListener((view, position) -> {
            if (view.getId() == R.id.iv_play) {
                PlayManager.play(0, songList, Constants.PLAYLIST_LOCAL_ID);
            } else {
                toFragment(position);
            }
        });
    }

    @Override
    public void showPlaylist(List<NewsList> playlists) {
        mAdapter.setList(playlists);
        mAdapter.notifyDataSetChanged();
        if (playlists.size() == 0) {
            // TODO: 2018/12/18 这里显示空状态
        }
    }

    @Override
    public void showHistory(List<Music> musicList) {
        mBinding.get().historyView.setSongsNum(musicList.size(), 1);
        mBinding.get().historyView.setOnItemClickListener((view, position) -> {
            if (view.getId() == R.id.iv_play) {
                PlayManager.play(0, musicList, Constants.PLAYLIST_HISTORY_ID);
            } else {
                toFragment(position);
            }
        });
    }

    @Override
    public void showLoveList(List<Music> musicList) {
        mBinding.get().favoriteView.setSongsNum(musicList.size(), 2);
        mBinding.get().favoriteView.setOnItemClickListener((view, position) -> {
            if (view.getId() == R.id.iv_play) {
                PlayManager.play(0, musicList, Constants.PLAYLIST_LOVE_ID);
            } else {
                toFragment(position);
            }
        });
    }

    @Override
    public void showDownloadList(List<Music> musicList) {
        mBinding.get().downloadView.setSongsNum(musicList.size(), 3);
        mBinding.get().downloadView.setOnItemClickListener((view, position) -> {
            if (view.getId() == R.id.iv_play) {
                PlayManager.play(0, musicList, Constants.PLAYLIST_DOWNLOAD_ID);
            } else {
                toFragment(position);
            }
        });
    }

    @Override
    public void showError(String message, boolean showRetryButton) {
        // TODO: 2018/12/18 错误处理
        ToastUtils.getInstance().showToast("--我的界面--" + message);
    }

    private void toFragment(int position) {
        switch (position) {
            case 0:
                NavigationHelper.navigateToLocalMusic(getBaseActivity(), null);
                break;
            case 1:
                NavigationHelper.navigateToPlaylist(getActivity(), PlaylistLoader.getHistoryPlaylist(), null);

                break;
            case 2:
                NavigationHelper.navigateToPlaylist(getActivity(), PlaylistLoader.getFavoritePlaylist(), null);

                break;
            case 3:
                NavigationHelper.navigateToDownload(getBaseActivity(), false, null);
                break;
        }
    }

    private void listener() {
        mBinding.get().playlistAddIv.setOnClickListener(v -> {
//                if (UserStatus.getLoginStatus()) {
//                    val dialog = CreatePlaylistDialog.newInstance()
//                    dialog.show(childFragmentManager, TAG_CREATE)
//                } else {
//                    ToastUtils.show(getString(R.string.prompt_login))
//                }
            ToastUtils.getInstance().showToast("这块需要登录暂时不做");
        });
        mBinding.get().playlistManagerIv.setOnClickListener(v -> {
            //
            ToastUtils.getInstance().showToast("开发中……");
//            startActivity(new Intent(getBaseActivity(),PlaylistManagerActivity.class));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
            mSubscribe = null;
        }
    }

    @Override
    public void detachView() {

    }

}
