package com.test.admin.conurbations.activitys;

/**
 * Created by ZQiong on 2018/12/26.
 */
public interface BaseViewImpl {
//    //显示进度中
//    void showLoading();
//
//    //隐藏进度
//    void hideLoading();

    //隐藏进度
    void showError(String message);

    //网络请求完毕，不管成功失败
    void showFinishState();
}
