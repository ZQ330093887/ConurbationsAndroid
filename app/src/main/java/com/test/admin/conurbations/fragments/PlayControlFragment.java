package com.test.admin.conurbations.fragments;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.IPlayContract;
import com.test.admin.conurbations.adapter.BottomMusicAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.PlayControlMenuBinding;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.StatusChangedEvent;
import com.test.admin.conurbations.player.FloatLyricViewManager;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.presenter.PlayPresenter;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.utils.LogUtil;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.utils.TransitionAnimationUtils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 播放器
 * Created by ZQiong on 2018/12/9.
 */
public class PlayControlFragment extends BaseFragment<PlayControlMenuBinding>
        implements SeekBar.OnSeekBarChangeListener, IPlayContract {
    private PlayPresenter mPresenter;
    private List<Music> musicList;
    private Disposable subscribe;
    private BottomMusicAdapter mAdapter;
    private ObjectAnimator coverAnimator;

    public static PlayControlFragment newInstance() {
        Bundle args = new Bundle();
        PlayControlFragment fragment = new PlayControlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.play_control_menu;
    }

    @Override
    protected void initData(Bundle bundle) {
        mPresenter = new PlayPresenter();
        mPresenter.attachView(this);
        showLyric(FloatLyricViewManager.lyricInfo, true);
        updatePlayStatus(PlayManager.isPlaying());
        initSongList();
        loadData();

        subscribe = RxBus.getDefault().toObservable(Event.class).subscribe(event -> {
            switch (event.eventType) {
                case Constants.META_CHANGED_EVENT:
                    Music music = (Music) event.body;
                    mPresenter.updateNowPlaying(music, false, getActivity());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mBinding.get().progressBar.setProgress(PlayManager.getCurrentPosition(), true);
                    } else {
                        mBinding.get().progressBar.setProgress(PlayManager.getCurrentPosition());
                    }
                    mBinding.get().progressBar.setMax(PlayManager.getDuration());
                    initSongList();

                    break;
                case Constants.STATUS_CHANGED_EVENT:
                    getBaseActivity().runOnUiThread(() -> {
                        StatusChangedEvent state = (StatusChangedEvent) event.body;
                        mBinding.get().playPauseView.setLoading(!state.isPrepared);
                        updatePlayStatus(state.isPlaying);
                    });
                    break;
                case Constants.PLAYLIST_LOVE_ID:
                    updatePlayMode();
                    break;
            }
        });
    }


    protected void loadData() {
        Music music = PlayManager.getPlayingMusic();
        if (mPresenter != null) {
            mPresenter.updateNowPlaying(music, true, getBaseActivity());
        }
    }

    protected void listener() {
        mBinding.get().bottomPlayRcv.setOnClickListener(v -> {
            //
            NavigationHelper.navigateToPlaying(getBaseActivity(), null);
        });
        mBinding.get().playQueueIv.setOnClickListener(v -> {
            PlayQueueDialog.newInstance().showIt(getBaseActivity());
        });
        mBinding.get().playPauseView.setOnClickListener(v -> {
            PlayManager.playPause();
        });
    }

    private void setPlayStatus() {
        if (coverAnimator != null) {
            if (PlayManager.isPlaying()) {
                if (coverAnimator.isStarted()) {
                    coverAnimator.resume();
                } else {
                    coverAnimator.start();
                }
            } else {
                coverAnimator.pause();
            }
        }
    }

    @Override
    public void detachView() {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (PlayManager.isPlaying() || PlayManager.isPause()) {
            int progress = seekBar.getProgress();
            PlayManager.seekTo(progress);
        } else {
            seekBar.setProgress(0);
        }
    }

    @Override
    public void setPlayingBitmap(Bitmap albumArt) {
    }

    @Override
    public void setPlayingBg(Drawable albumArt, boolean isInit) {
        if (isInit) {
            mBinding.get().album.setImageDrawable(albumArt);
        } else {
            //加载背景图过度
            TransitionAnimationUtils.startChangeAnimation(mBinding.get().album, albumArt);
        }
    }

    @Override
    public void showLyric(String lyric, boolean init) {
    }

    @Override
    public void updatePlayStatus(boolean isPlaying) {
        if (isPlaying && !mBinding.get().playPauseView.isPlaying()) {
            mBinding.get().playPauseView.play();
        } else if (!isPlaying && mBinding.get().playPauseView.isPlaying()) {
            mBinding.get().playPauseView.pause();
        }

        //设置播放状态
        setPlayStatus();
    }

    @Override
    public void updatePlayMode() {
    }

    @Override
    public void updateProgress(long progress, long max) {
        if (mBinding.get() == null) return;
        mBinding.get().progressBar.setProgress((int) progress);
        mBinding.get().progressBar.setMax((int) max);
    }

    @Override
    public void showNowPlaying(Music music) {
        mBinding.get().getRoot().setVisibility(music != null ? View.VISIBLE : View.GONE);

        if (coverAnimator != null) {
            coverAnimator.cancel();
            coverAnimator.start();
        }
    }

    /**
     * 初始化歌曲列表
     */
    private void initSongList() {
        if (musicList == null) {
            musicList = new ArrayList<>();
        }
        musicList.clear();
        musicList.addAll(PlayManager.getPlayList());
        if (mAdapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            mBinding.get().bottomPlayRcv.setLayoutManager(layoutManager);
            mAdapter = new BottomMusicAdapter(getActivity());
            mAdapter.setList(musicList);
            PagerSnapHelper snap = new PagerSnapHelper();
            snap.attachToRecyclerView(mBinding.get().bottomPlayRcv);
            mBinding.get().bottomPlayRcv.setAdapter(mAdapter);
            mBinding.get().bottomPlayRcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        int first = manager.findFirstVisibleItemPosition();
                        int last = manager.findLastVisibleItemPosition();
                        LogUtil.e("Scroll", first + "-" + last);
                        if (first == last && first != PlayManager.position()) {
                            PlayManager.play(first);
                        }
                    }
                }

            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
        initAlbumPic(mAdapter.coverImageView);
        mBinding.get().bottomPlayRcv.scrollToPosition(PlayManager.position());
        listener();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (coverAnimator != null) {
            coverAnimator.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (coverAnimator != null && coverAnimator.isPaused() && PlayManager.isPlaying()) {
            coverAnimator.resume();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (coverAnimator != null && coverAnimator.isRunning()) {
            coverAnimator.pause();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
            subscribe = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        coverAnimator = null;
//        EventBus.getDefault().unregister(this);
    }

    /**
     * 旋转动画
     */
    public void initAlbumPic(View view) {
        if (view == null) return;
        coverAnimator = ObjectAnimator.ofFloat(view, "rotation", 0, 359);
        coverAnimator.setDuration(20 * 1000);
        coverAnimator.setRepeatCount(-1);
        coverAnimator.setRepeatMode(ObjectAnimator.RESTART);
        coverAnimator.setInterpolator(new LinearInterpolator());
    }
}
