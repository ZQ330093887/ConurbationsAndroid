package com.test.admin.conurbations.presenter;

/**
 * Created by ZQiong on 2018/10/18.
 */

public interface Presenter<V> {
    void attachView(V view);

    boolean isAttached();

    void detachView();
}
