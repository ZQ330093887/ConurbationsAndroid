package com.test.admin.conurbations.widget.controller;

import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dueeeke.videoplayer.controller.BaseVideoController;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.util.L;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.utils.ToastUtils;

public class DouYinController extends BaseVideoController {

    private ImageView thumb;
    private View mRootview;
    private ImageView mIvPlay;

    private boolean isSelected = false;

    public DouYinController(@NonNull Context context) {
        super(context);
    }

    public DouYinController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DouYinController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_douyin_controller;
    }

    @Override
    protected void initView() {
        controllerView = LayoutInflater.from(getContext()).inflate(getLayoutId(), this);
        thumb = controllerView.findViewById(R.id.iv_thumb);
        mRootview = controllerView.findViewById(R.id.rootview);
        mIvPlay = controllerView.findViewById(R.id.iv_play);


        mRootview.setOnClickListener(v -> {
            if (isSelected) {
                mediaPlayer.start();
            } else {
                mediaPlayer.pause();
            }
            isSelected = !isSelected;
        });


    }

    @Override
    public void setPlayState(int playState) {
        super.setPlayState(playState);

        switch (playState) {
            case IjkVideoView.STATE_IDLE:
                L.e("STATE_IDLE");
                thumb.setVisibility(VISIBLE);
                mIvPlay.setVisibility(View.GONE);
                break;
            case IjkVideoView.STATE_PLAYING:
                L.e("STATE_PLAYING");
                thumb.setVisibility(GONE);
                mIvPlay.setVisibility(View.GONE);
                break;
            case IjkVideoView.STATE_PREPARED:
                L.e("STATE_PREPARED");
                break;
            case IjkVideoView.STATE_PAUSED:
                mIvPlay.setVisibility(View.VISIBLE);
                mIvPlay.setSelected(false);
                break;
            case IjkVideoView.STATE_ERROR:
                thumb.setVisibility(VISIBLE);
                mIvPlay.setVisibility(View.GONE);
                ToastUtils.getInstance().showToast("播放错误");
                break;
        }
    }

    public ImageView getThumb() {
        return thumb;
    }


    public View getRootview() {
        return mRootview;
    }

    public ImageView getIvPlay() {
        return mIvPlay;
    }


    public void setSelect(boolean selected) {
        isSelected = selected;
    }

}
