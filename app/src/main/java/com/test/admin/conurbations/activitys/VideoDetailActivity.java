package com.test.admin.conurbations.activitys;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.databinding.ActivityVideoDetailBinding;
import com.test.admin.conurbations.model.entity.LeVideoData;
import com.test.admin.conurbations.utils.DateUtils;
import com.test.admin.conurbations.utils.DialogUtils;
import com.test.admin.conurbations.utils.StatusBarUtil;
import com.test.admin.conurbations.utils.bigImageView.view.ImagePreviewActivity;

/**
 * Created by zhouqiong on 2019/4/3
 */

public class VideoDetailActivity extends BaseActivity<ActivityVideoDetailBinding> {

    public static final String VIDEO_DATA = "video_data";

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
        LeVideoData item = getIntent().getParcelableExtra(VIDEO_DATA);
        //传递数据之前已经判断了item是否为空，这里不在做判断
        PlayerConfig config = new PlayerConfig.Builder().setLooping().enableCache().build();
        mBinding.videoView.setPlayerConfig(config);
        mBinding.videoView.setUrl(item.videoPlayUrl);
        mBinding.videoView.setScreenScale(IjkVideoView.SCREEN_SCALE_DEFAULT);
        mBinding.videoView.start();


        Glide.with(this).load(item.coverImgUrl).dontAnimate().into(mBinding.ivUserAvatar);
        mBinding.tvVideoTitle.setText(item.title);
        mBinding.tvUsername.setText(item.authorName);
        mBinding.tvPlayCount.setText(DateUtils.formatNumber(item.playCount) + "播放");
        mBinding.tvLikeCount.setText(DateUtils.formatNumber(item.likeCount) + "赞");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.videoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.videoView.release();
    }
}
