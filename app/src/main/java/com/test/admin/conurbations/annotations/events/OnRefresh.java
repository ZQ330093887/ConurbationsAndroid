package com.test.admin.conurbations.annotations.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhouqiong on 2017/4/1.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ListenerMethod("android.support.v4.widget.SwipeRefreshLayout$OnRefreshListener")
public @interface OnRefresh {
    String[] value();
}
