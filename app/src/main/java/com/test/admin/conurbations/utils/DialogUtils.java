package com.test.admin.conurbations.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by zhouqiong on 2017/2/23.
 */

public class DialogUtils {

    private static Dialog mProgressDialog;

    public static void showProgressDialog(final Context context) {
        CommonUtil.ensureUiThread();
        if (context == null
                || mProgressDialog != null) {
            return;
        }
        mProgressDialog = LoadingDialog.newLoadingDialog(context);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mProgressDialog = null;
            }
        });
        mProgressDialog.show();
    }

    public static void showProgressDialog(final Context context, String message) {
        CommonUtil.ensureUiThread();
        if (context == null
                || mProgressDialog != null
                || message == null) {
            return;
        }
        mProgressDialog = LoadingDialog.newLoadingDialog(context, message);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mProgressDialog = null;
            }
        });
        mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        CommonUtil.ensureUiThread();
        if (mProgressDialog == null) {
            return;
        }
        mProgressDialog.setOnDismissListener(null);
        mProgressDialog.dismiss();
        mProgressDialog = null;

    }
}
