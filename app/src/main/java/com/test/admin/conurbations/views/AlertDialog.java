package com.test.admin.conurbations.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.test.admin.conurbations.R;


public class AlertDialog {
    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_msg;
    private TextView txt_error_toast;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView img_line;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private EditText et_msg;
    private EditText et_msgChart;

    private View buttons;

    public AlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    private View contentView;

    public AlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialog, null);
        contentView = view;
        // 获取自定义Dialog布局中的控件
        LinearLayout lLayout_bg = view.findViewById(R.id.lLayout_bg);
        txt_title = view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        txt_error_toast = view.findViewById(R.id.tv_error_toast);
        txt_error_toast.setVisibility(View.GONE);
        et_msg = view.findViewById(R.id.et_msg);
        et_msg.setVisibility(View.GONE);
        et_msgChart = view.findViewById(R.id.et_msg_chart);
        et_msgChart.setVisibility(View.GONE);
        btn_neg = view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line = view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
//        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.78), LayoutParams.WRAP_CONTENT));

        return this;
    }

    private TextView limitTv;

    public AlertDialog builderLimit() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialog_limit, null);
        contentView = view;
        limitTv = view.findViewById(R.id.tv_tag_length);
        // 获取自定义Dialog布局中的控件
        LinearLayout lLayout_bg = view.findViewById(R.id.lLayout_bg);
        txt_title = view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        txt_error_toast = view.findViewById(R.id.tv_error_toast);
        txt_error_toast.setVisibility(View.GONE);
        et_msg = view.findViewById(R.id.et_msg);
        et_msg.setVisibility(View.GONE);
        et_msgChart = view.findViewById(R.id.et_msg_chart);
        et_msgChart.setVisibility(View.GONE);
        btn_neg = view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line = view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
//        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

        return this;
    }

    public TextView getLimitTv() {
        return limitTv;
    }

    public View getContentView() {
        return contentView;
    }

    public void setCanceledOnTouchOutside(boolean flag) {
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
    }

    public EditText setEditer() {
        txt_msg.setVisibility(View.GONE);
        showMsg = true;
        txt_msg = et_msg;
        return et_msg;
    }

    public String getText() {
        return txt_msg.getText().toString();
    }

    public TextView getMsgTextView() {
        return txt_msg;
    }

    public EditText setEditerChart() {
        txt_msg.setVisibility(View.GONE);
        et_msg.setVisibility(View.GONE);
        showMsg = true;
        txt_msg = et_msgChart;
        return et_msgChart;
    }

    public void setLeft() {
        txt_msg.setGravity(Gravity.LEFT);
    }

    public AlertDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        txt_title.setVisibility(View.VISIBLE);
        return this;
    }

    public AlertDialog setTitle(String title, int gravity) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setGravity(gravity);
        return this;
    }

    public AlertDialog setMsg(CharSequence msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        txt_msg.setVisibility(View.VISIBLE);
        return this;
    }

    public AlertDialog setErrorToast(CharSequence msg) {
        txt_error_toast.setText(msg);
        txt_error_toast.setVisibility(View.VISIBLE);
        return this;
    }

    public AlertDialog setMsg(CharSequence msg, int gravity) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        txt_msg.setGravity(gravity);
        txt_msg.setVisibility(View.VISIBLE);
        return this;
    }

    public AlertDialog hiddenButtons() {
        buttons.setVisibility(View.GONE);
        return this;
    }

    public AlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public AlertDialog setPositiveButton(String text,
                                         final OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setVisibility(View.VISIBLE);
        btn_pos.setOnClickListener(v -> {
            if (listener != null) {
                if (showMsg)
                    listener.onClick(txt_msg);
                else
                    listener.onClick(v);
            }
            dialog.dismiss();
        });
        return this;
    }

    public AlertDialog setPositiveButton(String text,
                                         final OnClickListener listener, boolean isDismissDialog) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setVisibility(View.VISIBLE);
        btn_pos.setOnClickListener(v -> {
            if (listener != null) {
                if (showMsg)
                    listener.onClick(txt_msg);
                else
                    listener.onClick(v);
            }
            if (isDismissDialog) {
                dialog.dismiss();
            }
        });
        return this;
    }


    public AlertDialog setNegativeButton(String text,
                                         final OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(v);
            dialog.dismiss();
        });
        btn_neg.setVisibility(View.VISIBLE);
        return this;
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            txt_title.setText("提示");
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
            btn_pos.setOnClickListener(v -> dialog.dismiss());
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }
    }

    public void show() {
        setLayout();
        dialog.show();
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
