package com.test.admin.conurbations.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.tt.whorlviewlibrary.WhorlView;


public class LoadingDialog extends Dialog {
    private TextView mDesView;
    private WhorlView mWhorlView;

    public LoadingDialog(Context context) {
        super(context);
        init(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.loading_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        mDesView = findViewById(R.id.des_view);
        mWhorlView = findViewById(R.id.whorl);
        mWhorlView.start();
    }

    public void setDes(String des, int visibility) {
        if (null != mDesView) {
            mDesView.setText(des);
            mDesView.setVisibility(visibility);
        }
    }


    public static LoadingDialog newLoadingDialog(Context context) {
        return newLoadingDialog(context, "正在加载...");
    }

    public static LoadingDialog newLoadingDialog(Context context, String des) {
        final LoadingDialog dialog = new LoadingDialog(context, R.style.common_dialog);
        if (false == TextUtils.isEmpty(des)) {
            dialog.setDes(des, View.VISIBLE);
        } else {
            dialog.setDes("", View.GONE);
        }
        // sign_dialog.show();
        return dialog;
    }

}
