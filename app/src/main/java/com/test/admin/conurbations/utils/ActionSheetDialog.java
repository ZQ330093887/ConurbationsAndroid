package com.test.admin.conurbations.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.test.admin.conurbations.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * /**
 * Created by ZQiong on 2017/8/10.
 * 精仿iOSAlertViewController控件
 * 底部弹出dialog（类似相册，拍照，取消等功能）
 */
public class ActionSheetDialog {
    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_cancel;
    private ImageView mImageView;
    private LinearLayout lLayout_content;
    private ScrollView sLayout_content;
    private boolean showTitle = false;
    private List<SheetItem> sheetItemList;
    private Display display;

    public ActionSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ActionSheetDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_actionsheet, null);

        view.setMinimumWidth(display.getWidth());

        sLayout_content = view.findViewById(R.id.sLayout_content);
        lLayout_content = view.findViewById(R.id.lLayout_content);
        txt_title = view.findViewById(R.id.txt_title);
        mImageView = view.findViewById(R.id.iv_img_info);
        txt_cancel = view.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(v -> dialog.dismiss());

        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    public ActionSheetDialog setTitle(String title) {
        showTitle = true;
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(title);
        return this;
    }

    public ActionSheetDialog setImage(@DrawableRes int resId) {
        mImageView.setVisibility(View.VISIBLE);
        mImageView.setImageResource(resId);
        return this;
    }

    public ActionSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
    }

    public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public ActionSheetDialog addSheetItem(String strItem, SheetItemColor color,
                                          OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<>();
        }
        sheetItemList.add(new SheetItem(strItem, color, listener));
        return this;
    }

    public ActionSheetDialog addSheetItem(String[] strItem, SheetItemColor color,
                                          OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<>();
        }
        for (String item : strItem)
            sheetItemList.add(new SheetItem(item, color, listener));
        return this;
    }

    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }

        int size = sheetItemList.size();

        if (size >= 7) {
            LayoutParams params = (LayoutParams) sLayout_content
                    .getLayoutParams();
            params.height = display.getHeight() / 2;
            sLayout_content.setLayoutParams(params);
        }

        for (int i = 1; i <= size; i++) {
            final int index = i;
            SheetItem sheetItem = sheetItemList.get(i - 1);
            String strItem = sheetItem.name;
            SheetItemColor color = sheetItem.color;
            final OnSheetItemClickListener listener = sheetItem.itemClickListener;

            TextView textView = new TextView(context);
            textView.setText(strItem);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                } else {
                    textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
                }
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                }
            }
            if (color == null) {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue
                        .getName()));
            } else {
                textView.setTextColor(Color.parseColor(color.getName()));
            }
            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5f);
            textView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, height));
            textView.setOnClickListener(v -> {
                listener.onClick(index - 1);
                dialog.dismiss();
            });

            lLayout_content.addView(textView);
        }
    }

    public void show() {
        setSheetItems();
        dialog.show();
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }

    public class SheetItem {
        String name;
        OnSheetItemClickListener itemClickListener;
        SheetItemColor color;

        public SheetItem(String name, SheetItemColor color,
                         OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }
    }

    public enum SheetItemColor {
        Blue("#037BFF"), Red("#FD4A2E"), PeStyle("#faa41d"), Black("#030303");

        private String name;

        private SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    /**
     * 简便调用底部弹框
     *
     * @param list 显示条目集合
     * @param showFiledName 集合中bean中要显示的字段名 如：要显示bean.name，传入参数为"name"
     * @param listener 点击回调
     * @param <T>
     */
    private List list;//临时保存集合

    public <T> void showActionSheetDialog(final List<T> list, String showFiledName,
                                          final OnItemClickListener<T> listener) {
        this.builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true);
        this.list = list;
        try {
            Field field = list.get(0).getClass().getDeclaredField(showFiledName);
            for (T t : list) {
                field.setAccessible(true);
                String showName = (String) field.get(t);
                field.setAccessible(false);
                this.addSheetItem(showName, SheetItemColor.Blue,
                        which -> listener.onClick(list.get(which)));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            this.addSheetItem("showFiledName传错了", SheetItemColor.Blue,
                    which -> {

                    });
            this.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.show();
    }

    public interface OnItemClickListener<T> {
        void onClick(T bean);
    }
}
