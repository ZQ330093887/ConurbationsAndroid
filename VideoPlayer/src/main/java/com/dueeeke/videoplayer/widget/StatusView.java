package com.dueeeke.videoplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dueeeke.videoplayer.R;

/**
 * 错误提示，网络提示
 * Created by Devlin_n on 2017/4/13.
 */

public class StatusView extends LinearLayout {

    private TextView tvMessage;
    private TextView btnAction;
    private ImageView ivClose;
    private float downX;
    private float downY;

    public StatusView(Context context) {
        this(context, null);
    }

    public StatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.layout_status_view, this);
        tvMessage = root.findViewById(R.id.message);
        btnAction = root.findViewById(R.id.status_btn);
        ivClose = root.findViewById(R.id.btn_close);
        this.setBackgroundResource(android.R.color.black);
        setClickable(true);
//        if (PlayerConstants.IS_START_FLOAT_WINDOW) {
//            ivClose.setVisibility(VISIBLE);
//            ivClose.setOnClickListener(v -> {
//                Intent intent = new Intent(getContext(), BackgroundPlayService.class);
//                intent.putExtra(KeyUtil.ACTION, PlayerConstants.COMMAND_STOP);
//                getContext().getApplicationContext().startService(intent);
//            });
//        }
    }

    public void setMessage(String msg) {
        if (tvMessage != null) tvMessage.setText(msg);
    }

    public void setButtonTextAndAction(String text, OnClickListener listener) {
        if (btnAction != null) {
            btnAction.setText(text);
            btnAction.setOnClickListener(listener);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                // True if the child does not want the parent to intercept touch events.
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float absDeltaX = Math.abs(ev.getX() - downX);
                float absDeltaY = Math.abs(ev.getY() - downY);
                if (absDeltaX > ViewConfiguration.get(getContext()).getScaledTouchSlop() ||
                        absDeltaY > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
