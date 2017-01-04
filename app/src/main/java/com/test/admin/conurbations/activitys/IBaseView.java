package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.view.View;

/**
 * Created by waly6 on 2015/9/29.
 */
public interface IBaseView {
    void startActivityAndFinishWithoutTransition(Class<?> cls, Bundle bundle, View view);
}
