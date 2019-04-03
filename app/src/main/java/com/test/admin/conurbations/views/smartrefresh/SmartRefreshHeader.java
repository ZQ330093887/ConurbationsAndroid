package com.test.admin.conurbations.views.smartrefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.ArrowDrawable;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.test.admin.conurbations.R;

public class SmartRefreshHeader extends LinearLayout implements RefreshHeader {
    private TextView mHeaderText;//标题文本
    private ImageView mArrowView;//下拉箭头
    private ProgressBar mProgressView;//刷新动画视图

    private Space mSpace;

    public SmartRefreshHeader(Context context) {
        this(context, null);
    }

    public SmartRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        setGravity(Gravity.CENTER);
        mHeaderText = new TextView(context);

        mProgressView = (ProgressBar) LayoutInflater.from(getContext()).inflate(R.layout.layout_progressbar, null, false);


        mArrowView = new ImageView(context);
        mSpace = new Space(context);
        mArrowView.setImageDrawable(new ArrowDrawable());
        addView(mProgressView, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
        addView(mArrowView, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
        addView(mSpace, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
        addView(mHeaderText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setMinimumHeight(DensityUtil.dp2px(60));
    }

    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int maxDragHeight) {
        mProgressView.setVisibility(VISIBLE);
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        mProgressView.setVisibility(GONE);//隐藏动画
        if (success) {
            mHeaderText.setVisibility(VISIBLE);
            mHeaderText.setText("刷新完成");
        } else {
            mHeaderText.setText("刷新失败");
        }
        return 500;//延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mHeaderText.setText("下拉刷新");
                mSpace.setVisibility(VISIBLE);
                mHeaderText.setVisibility(VISIBLE);
                mArrowView.setVisibility(VISIBLE);//显示下拉箭头
                mProgressView.setVisibility(GONE);//隐藏动画
                mArrowView.animate().rotation(0);//还原箭头方向
                break;
            case Refreshing:
                mHeaderText.setText("");
                mSpace.setVisibility(GONE);
                mHeaderText.setVisibility(GONE);
                mProgressView.setVisibility(VISIBLE);//显示加载动画
                mArrowView.setVisibility(GONE);//隐藏箭头
                break;
            case ReleaseToRefresh:
                mSpace.setVisibility(VISIBLE);
                mHeaderText.setVisibility(VISIBLE);
                mHeaderText.setText("松开刷新");
                mArrowView.animate().rotation(180);//显示箭头改为朝上
                break;
        }
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean b, float v, int i, int i1, int i2) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
}
