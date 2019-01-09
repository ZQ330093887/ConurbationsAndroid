package com.test.admin.conurbations.utils;

import android.content.Context;
import android.view.View;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.views.AlertDialog;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class AlertDialogUtils {
    /**
     * 提示对话框显示tip
     */
    public static void showTipsDialog(Context context, String content, View.OnClickListener positiveListener) {
        AlertDialog alertDialog = new AlertDialog(context).builder();
        alertDialog.setTitle(context.getString(R.string.prompt));
        alertDialog.setMsg(content);
        alertDialog.setNegativeButton("取消", null);
        alertDialog.setPositiveButton("确认", positiveListener);
        alertDialog.show();
    }

    /**
     * 提示对话框显示tip
     */
    public static void showTipsDialog(Context context, String content) {
        AlertDialog alertDialog = new AlertDialog(context).builder();
        alertDialog.setTitle(context.getString(R.string.prompt));
        alertDialog.setMsg(content);
        alertDialog.setNegativeButton("取消", v -> alertDialog.dismiss());
        alertDialog.setPositiveButton("确认", v -> alertDialog.dismiss());
        alertDialog.show();
    }

    public static void showTipsDialog(Context context, String title, String content, View.OnClickListener positiveListener) {
        AlertDialog alertDialog = new AlertDialog(context).builder();
        alertDialog.setTitle(title);
        alertDialog.setMsg(content);
        alertDialog.setNegativeButton("取消", null);
        alertDialog.setPositiveButton("确认", positiveListener);
        alertDialog.show();
    }

    public static void showTipsDialog(Context context, String title, String content, View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        AlertDialog alertDialog = new AlertDialog(context).builder();
        alertDialog.setTitle(title);
        alertDialog.setMsg(content);
        alertDialog.setNegativeButton("取消", negativeListener);
        alertDialog.setPositiveButton("确认", positiveListener);
        alertDialog.show();
    }
}
