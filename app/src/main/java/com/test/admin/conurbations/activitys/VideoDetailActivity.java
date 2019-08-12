package com.test.admin.conurbations.activitys;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.DouYinAdapter;
import com.test.admin.conurbations.databinding.ActivityVideoDetailBinding;
import com.test.admin.conurbations.databinding.ItemVideoDetailBinding;
import com.test.admin.conurbations.model.entity.DouyinVideoListData;
import com.test.admin.conurbations.model.entity.LeVideoData;
import com.test.admin.conurbations.model.entity.RefreshEvent;
import com.test.admin.conurbations.presenter.VideoDetailPresenter;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.utils.DateUtils;
import com.test.admin.conurbations.utils.StatusBarUtil;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.WeakDataHolder;
import com.test.admin.conurbations.widget.VerticalViewPager;
import com.test.admin.conurbations.widget.controller.DouYinController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouqiong on 2019/4/3
 */

public class VideoDetailActivity extends BaseActivity<ActivityVideoDetailBinding> implements IVideoDetailView {

    public static final String VIDEO_DATA = "video_data";
    private List<LeVideoData> mList = new ArrayList<>();
    private List<View> mViews = new ArrayList<>();
    private int mPlayingPosition;
    private int position;
    private long max_cursor = 0;
    private int mCurrentItem;

    private IjkVideoView mIjkVideoView;
    private DouYinAdapter mDouYinAdapter;
    private DouYinController mDouYinController;
    private VideoDetailPresenter videoDetailPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initData(Bundle bundle) {
        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.darkMode(this, false);
        mList = (List<LeVideoData>) WeakDataHolder.getInstance().getData(VIDEO_DATA);
        position = getIntent().getIntExtra("position", -1);
        max_cursor = getIntent().getLongExtra("max_cursor", -1);

        mCurrentItem = position;

        mIjkVideoView = new IjkVideoView(this);
        PlayerConfig config = new PlayerConfig.Builder().setLooping().build();
        mIjkVideoView.setPlayerConfig(config);
        mDouYinController = new DouYinController(this);
        mIjkVideoView.setVideoController(mDouYinController);
        videoDetailPresenter = new VideoDetailPresenter(this);

        getImageData();

        mBinding.ivBack.setOnClickListener(v -> onBackPressed());

    }

    private void getImageData() {
        AddItemView();

        mDouYinAdapter = new DouYinAdapter(mViews);

        mBinding.verticalviewpager.setAdapter(mDouYinAdapter);

        if (position != -1) {
            mBinding.verticalviewpager.setCurrentItem(position);
        }


        mBinding.verticalviewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
                mIjkVideoView.pause();

                if (mCurrentItem == mList.size() - 1) {
                    ToastUtils.getInstance().showToast("加载中，请稍后");
                    if (videoDetailPresenter != null) {
                        videoDetailPresenter.getDouyinListData(VideoDetailActivity.this, max_cursor);
                    }
                }
                RxBus.getDefault().post(new RefreshEvent(mCurrentItem, max_cursor));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (mPlayingPosition == mCurrentItem) return;
                if (state == VerticalViewPager.SCROLL_STATE_IDLE) {
                    mIjkVideoView.release();
                    ViewParent parent = mIjkVideoView.getParent();
                    if (parent instanceof FrameLayout) {
                        ((FrameLayout) parent).removeView(mIjkVideoView);
                    }
                    startPlay();
                }
            }
        });


        mBinding.verticalviewpager.post(this::startPlay);
    }


    private void startPlay() {
        View view = mViews.get(mCurrentItem);
        FrameLayout frameLayout = view.findViewById(R.id.container);
        ImageView mCover = view.findViewById(R.id.cover_img);

        mDouYinController.setSelect(false);

        if (mCover != null && mCover.getDrawable() != null) {
            mDouYinController.getThumb().setImageDrawable(mCover.getDrawable());
        }

        ViewGroup parent = (ViewGroup) mIjkVideoView.getParent();

        if (parent != null) {
            parent.removeAllViews();
        }

        frameLayout.addView(mIjkVideoView);
        mIjkVideoView.setUrl(mList.get(mCurrentItem).videoPlayUrl);
        mIjkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_DEFAULT);
        mIjkVideoView.start();

        mPlayingPosition = mCurrentItem;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIjkVideoView.pause();
        if (mDouYinController != null) {
            mDouYinController.getIvPlay().setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIjkVideoView.resume();

        if (mDouYinController != null) {
            mDouYinController.setSelect(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIjkVideoView.release();
    }

    @Override
    public void setVideoDouYinData(DouyinVideoListData listData) {
        if (listData == null || listData.videoDataList == null || listData.videoDataList.size() == 0)
            return;


        max_cursor = listData.maxCursor;
        mList.addAll(listData.videoDataList);

        AddItemView();

        mDouYinAdapter.setmViews(mViews);
        mDouYinAdapter.notifyDataSetChanged();
    }

    private void AddItemView() {
        mViews.clear();//加载更多需要先清空原来的view

        for (LeVideoData item : mList) {
            ItemVideoDetailBinding videoDetailBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.item_video_detail, null, false);

            Glide.with(this).load(item.coverImgUrl).dontAnimate().into(videoDetailBinding.coverImg);
            Glide.with(this).load(item.authorImgUrl).dontAnimate().into(videoDetailBinding.ivUserAvatar);


            videoDetailBinding.tvVideoTitle.setText(item.title);
            videoDetailBinding.tvUsername.setText(item.authorName);
            videoDetailBinding.tvPlayCount.setText(String.format(DateUtils.formatNumber(item.playCount) + "%s", "播放"));
            videoDetailBinding.tvLikeCount.setText(String.format(DateUtils.formatNumber(item.likeCount) + "%s", "赞"));

            mViews.add(videoDetailBinding.getRoot());
        }
    }

    @Override
    public void showError(String msg) {
        ToastUtils.getInstance().showToast(msg);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RxBus.getDefault().post(new RefreshEvent(mCurrentItem, max_cursor));
    }
}
