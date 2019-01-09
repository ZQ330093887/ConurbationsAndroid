package com.test.admin.conurbations.widget.statuslayoutmanage;

import android.view.View;

/**
 * 状态布局中 view 的点击事件
 * Created by ZQiong on 2018/4/19.
 */

public interface OnStatusChildClickListener {

    /**
     * 空数据布局子 View 被点击
     *
     * @param view 被点击的 View
     */
    void onEmptyChildClick(View view);

    /**
     * 出错布局子 View 被点击
     *
     * @param view 被点击的 View
     */
    void onErrorChildClick(View view);

    /**
     * 自定义状态布局布局子 View 被点击
     *
     * @param view 被点击的 View
     */
    void onCustomerChildClick(View view);
}
