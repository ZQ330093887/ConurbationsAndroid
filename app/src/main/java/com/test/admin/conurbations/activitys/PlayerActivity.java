package com.test.admin.conurbations.activitys;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.MyPagerAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.ActivityPlayerBinding;
import com.test.admin.conurbations.fragments.BottomDialogFragment;
import com.test.admin.conurbations.fragments.PlayQueueDialog;
import com.test.admin.conurbations.fragments.QualitySelectDialog;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.StatusChangedEvent;
import com.test.admin.conurbations.player.FloatLyricViewManager;
import com.test.admin.conurbations.player.LyricView;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.presenter.PlayPresenter;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.utils.DateUtils;
import com.test.admin.conurbations.utils.LogUtil;
import com.test.admin.conurbations.utils.MusicUtils;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.utils.SPUtils;
import com.test.admin.conurbations.utils.SaveBitmapUtils;
import com.test.admin.conurbations.utils.StatusBarUtil;
import com.test.admin.conurbations.utils.TransitionAnimationUtils;
import com.test.admin.conurbations.utils.rom.OnlinePlaylistUtils;
import com.test.admin.conurbations.utils.rom.UIUtils;
import com.test.admin.conurbations.views.DepthPageTransformer;
import com.test.admin.conurbations.views.MultiTouchViewPager;
import com.test.admin.conurbations.views.MusicLyricDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by ZQiong on 2018/12/10.
 */
public class PlayerActivity extends BaseActivity<ActivityPlayerBinding> implements IPlayContract {

    private Music playingMusic;
    private View lyricView;
    private View coverView;
    private PlayPresenter mPresenter;
    private List<View> viewPagerContent;
    private LyricView mLyricView;
    private TextView mQualityTv;
    private ObjectAnimator coverAnimator;
    public Boolean isPause = true;
    private Disposable subscribe;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    protected void initData(Bundle bundle) {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, mBinding.toolbar);
        StatusBarUtil.darkMode(this, false);
        initView();
        LoadingData();
    }

    private void initView() {
        mPresenter = new PlayPresenter();
        mPresenter.attachView(this);
        mBinding.detailView.setAnimation(moveToViewLocation());
        updatePlayMode();
    }

    private void LoadingData() {
        setupViewPager(mBinding.viewPager);
        initAlbumPic(coverView.findViewById(R.id.civ_cover));
        mPresenter.updateNowPlaying(PlayManager.getPlayingMusic(), true, this);
        //初始加載歌詞
        //更新播放状态
        updatePlayStatus(PlayManager.isPlaying());
        showLyric(FloatLyricViewManager.lyricInfo, true);
        updateMusicType("");
    }

    /**
     * 添加到歌單
     */
    public void addToPlaylist(View view) {
        OnlinePlaylistUtils.INSTANCE.addToPlaylist(this, playingMusic);
    }

    /**
     * 添加到歌單
     */
    public void showSongComment(View view) {
//        Intent intent = new Intent(this,SongCommentActivity.class);
//        intent.putExtra("",playingMusic);
//        startActivity(intent);
    }

    /**
     * 分享歌曲
     */
    public void shareMusic(View view) {
        MusicUtils.qqShare(this, PlayManager.getPlayingMusic());
    }

    public void downloadMusic(View view) {
        QualitySelectDialog.newInstance(playingMusic).setDownload(true).show(this);
    }

    /**
     * 歌曲收藏
     */
    public void collectMusic(View view) {
        UIUtils.INSTANCE.collectMusic((ImageView) view, playingMusic);
    }

    /**
     * 打开播放队列
     */
    public void openPlayQueue(View view) {
        PlayQueueDialog.newInstance().showIt(this);
    }

    public void nextPlay(View view) {
        if (UIUtils.INSTANCE.isFastClick()) return;
        PlayManager.next();
    }

    public void prevPlay(View view) {
        if (UIUtils.INSTANCE.isFastClick()) return;
        PlayManager.prev();
    }

    public void changePlayMode(View view) {
        UIUtils.INSTANCE.updatePlayMode((ImageView) view, true);
    }

    @Override
    public void setPlayingBitmap(Bitmap albumArt) {
        ((ImageView) coverView.findViewById(R.id.civ_cover)).setImageBitmap(albumArt);
    }

    @Override
    public void setPlayingBg(Drawable albumArt, boolean isInit) {
        if (isInit) {
            mBinding.playingBgIv.setImageDrawable(albumArt);
        } else {
            //加载背景图过度
            TransitionAnimationUtils.startChangeAnimation(mBinding.playingBgIv, albumArt);
        }
    }

    @Override
    public void showLyric(String lyric, boolean init) {
        if (init) {
            //初始化歌词配置
            mLyricView.setTextSize(SPUtils.getFontSize());
            mLyricView.setHighLightTextColor(SPUtils.getFontColor());
            mLyricView.setTouchable(true);
            mLyricView.setOnPlayerClickListener((progress, content) -> {
                PlayManager.seekTo((int) progress);
                if (!PlayManager.isPlaying()) {
                    PlayManager.playPause();
                }
            });
        }
        mLyricView.setLyricContent(lyric);


        mBinding.searchLyricIv.setOnClickListener(v -> {
            MusicLyricDialog musicLyricDialog = new MusicLyricDialog();
            musicLyricDialog.setTitle(playingMusic.title);
            musicLyricDialog.setArtist(playingMusic.artist);
            musicLyricDialog.setDuration(PlayManager.getDuration());
            musicLyricDialog.setTextColorListener(integer -> {
                mLyricView.setHighLightTextColor(integer);
                return null;
            });
            musicLyricDialog.setTextSizeListener(integer -> {
                mLyricView.setTextSize(integer);
                return null;
            });
            musicLyricDialog.setLyricListener(s -> {
                mLyricView.setLyricContent(s);
                return null;
            });
            musicLyricDialog.show(getBaseActivity());
        });
    }

    @Override
    public void updatePlayStatus(boolean isPlaying) {
        if (isPlaying) {
            mBinding.playPauseIv.play();
            if (coverAnimator.isStarted()) {
                coverAnimator.resume();
            } else {
                coverAnimator.start();
            }

        } else {
            coverAnimator.pause();
            mBinding.playPauseIv.pause();
        }
    }

    @Override
    public void updatePlayMode() {
        UIUtils.INSTANCE.updatePlayMode(mBinding.playModeIv, false);
    }

    @Override
    public void updateProgress(long progress, long max) {
        if (!isPause) {
            mBinding.progressSb.setProgress((int) progress);
            mBinding.progressSb.setMax((int) max);
            mBinding.progressTv.setText(DateUtils.formatTime(progress));
            mBinding.durationTv.setText(DateUtils.formatTime(max));
            mLyricView.setCurrentTimeMillis(progress);
        }
    }

    @Override
    public void showNowPlaying(Music music) {
        if (music == null) return;

        playingMusic = music;
        //更新标题
        mBinding.titleIv.setText(playingMusic.title);
        mBinding.subTitleTv.setText(playingMusic.artist);
        //更新图片
        SaveBitmapUtils.loadBigImageView(this, playingMusic, bitmap -> {
            //
            ((ImageView) coverView.findViewById(R.id.civ_cover)).setImageBitmap(bitmap);
        });
        //更新类型
        updateMusicType(playingMusic.type);
        //更新收藏状态
        mBinding.collectIv.setImageResource(playingMusic.isLove ? R.drawable.item_favorite_love : R.drawable.item_favorite);

        //更新下载状态
//        mBinding.downloadIv.setVisibility(playingMusic.isDl ? View.VISIBLE : View.GONE);
        //隐藏显示歌曲评论


        if (playingMusic.type.equals(Constants.XIAMI) || playingMusic.
                type.equals(Constants.QQ) || playingMusic.type.equals(Constants.NETEASE)) {
            mBinding.songCommentTv.setVisibility(View.VISIBLE);
        } else {
            mBinding.songCommentTv.setVisibility(View.GONE);
        }

        coverAnimator.cancel();
        coverAnimator.start();
    }

    private void setupViewPager(MultiTouchViewPager viewPager) {
        //初始化View
        coverView = LayoutInflater.from(this).inflate(R.layout.frag_player_coverview, viewPager, false);
        lyricView = LayoutInflater.from(this).inflate(R.layout.frag_player_lrcview, viewPager, false);
        mLyricView = lyricView.findViewById(R.id.lyricShow);
        mQualityTv = coverView.findViewById(R.id.tv_quality);
        if (viewPagerContent == null) {
            viewPagerContent = new ArrayList<>();
        }
        viewPagerContent.clear();
        viewPagerContent.add(coverView);
        viewPagerContent.add(lyricView);
        listener();
        MyPagerAdapter mAdapter = new MyPagerAdapter(viewPagerContent);
        viewPager.setAdapter(mAdapter);
        viewPager.setPageTransformer(false, new DepthPageTransformer());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.d("PlayControlFragment", "--$position");
                if (position == 0) {
                    mBinding.searchLyricIv.setVisibility(View.GONE);
                    mBinding.operateSongIv.setVisibility(View.VISIBLE);
                    mLyricView.setIndicatorShow(false);
                } else {
                    mBinding.searchLyricIv.setVisibility(View.VISIBLE);
                    mBinding.operateSongIv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mQualityTv.setOnClickListener(v -> {
            QualitySelectDialog.newInstance(playingMusic).setDownload(false)
                    .setChangeSuccessListener(name -> mQualityTv.setText(name)).show(this);
        });

        coverView.findViewById(R.id.tv_sound_effect).setOnClickListener(v -> {
            NavigationHelper.navigateToSoundEffect(PlayerActivity.this);
        });
    }


    public void listener() {
        mBinding.backIv.setOnClickListener(v -> closeActivity());
        mBinding.progressSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PlayManager.seekTo(seekBar.getProgress());
                mLyricView.setCurrentTimeMillis(seekBar.getProgress());
            }
        });

        mBinding.playPauseIv.setOnClickListener(v -> PlayManager.playPause());
        mBinding.operateSongIv.setOnClickListener(v -> {
            BottomDialogFragment.newInstance(playingMusic)
                    .show(getBaseActivity());
        });

        subscribe = RxBus.getDefault().toObservable(Event.class).subscribe(event -> {
            if (event.eventType.equals(Constants.META_CHANGED_EVENT)) {
                Music music = (Music) event.body;
                mPresenter.updateNowPlaying(music, false, this);
            } else if (event.eventType.equals(Constants.STATUS_CHANGED_EVENT)) {
                runOnUiThread(() -> {
                    StatusChangedEvent state = (StatusChangedEvent) event.body;
                    mBinding.playPauseIv.setLoading(!state.isPrepared);
                    updatePlayStatus(state.isPlaying);
                });
            } else if (event.eventType.equals(Constants.PLAYLIST_LOVE_ID)) {
                updatePlayMode();
            }
        });
    }

    /**
     * 更新歌曲類型
     */
    private void updateMusicType(String type) {

        String value = "";
        switch (type) {
            case Constants.QQ:
                value = getString(R.string.res_qq);
                break;
            case Constants.BAIDU:
                value = getString(R.string.res_baidu);
                break;
            case Constants.NETEASE:
                value = getString(R.string.res_wangyi);
                break;
            case Constants.XIAMI:
                value = getString(R.string.res_xiami);
                break;
            default:
                value = getString(R.string.res_local);
                break;
        }
        String quality = "标准";
        switch (playingMusic.quality) {
            case 128000:
                quality = "标准";
                break;
            case 192000:
                quality = "较高品质";
                break;
            case 320000:
                quality = "HQ高品质";
                break;
            case 999000:
                quality = "SQ无损品质";
                break;

        }
        mQualityTv.setText(quality);
        if (!TextUtils.isEmpty(value)) {
            ((TextView) coverView.findViewById(R.id.tv_source)).setText(value);
        }
    }

    private TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 初始化旋转动画
     */
    private void initAlbumPic(View view) {
        if (view == null) return;
        coverAnimator = ObjectAnimator.ofFloat(view, "rotation", 0F, 359F);
        coverAnimator.setDuration(20 * 1000);
        coverAnimator.setRepeatCount(-1);
        coverAnimator.setRepeatMode(ObjectAnimator.RESTART);
        coverAnimator.setInterpolator(new LinearInterpolator());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }

    private void closeActivity() {
        overridePendingTransition(0, 0);
        ActivityCompat.finishAfterTransition(this);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coverAnimator.cancel();
        coverAnimator = null;

        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
            subscribe = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (coverAnimator != null && coverAnimator.isPaused() && PlayManager.isPlaying()) {
            coverAnimator.resume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isPause = true;
        coverAnimator.pause();
    }
}
