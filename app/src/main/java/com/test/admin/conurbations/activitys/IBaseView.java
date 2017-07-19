package com.test.admin.conurbations.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by zhouqiong on 2017/2/27.
 */
public interface IBaseView {
    BaseActivity getBaseActivity();

    void startActivity(Class<?> cls);

    void startActivityAndClear(Class<?> cls);

    void startActivity(Class<?> cls, Bundle bundle);

    void startActivityAndFinishWithoutTransition(Class<?> cls, Bundle bundle);

    void startActivityAndFinish(Class<?> cls);

    void startActivityAndFinishWithoutTransition(Class<?> cls);

    void startActivityAndFinish(Class<?> cls, Bundle bundle);

    void startActivityAndFinishWithOutObservable(Class<?> cls);

    void startActivityForResultWithoutTransition(Class<?> cls);

    void startActivityForResultWithoutTransition(Class<?> cls, int requestCode);

    void startActivityForResult(Class<?> cls);

    void startActivityForResult(Class<?> cls, Bundle bundle);

    void startActivityForResult(Class<?> cls, int requestCode);

    void startActivityForResult(Class<?> cls, int requestCode, Bundle bundle);

    Activity getRootActivity();

    Context getContext();

    Context getApplicationContext();

    void finishActivity();

    void detachView();
}
