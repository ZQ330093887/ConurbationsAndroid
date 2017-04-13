package com.test.admin.conurbations.annotations;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by waly6 on 2015/12/7.
 */
public class DynamicHandler implements InvocationHandler {

    private Object target;
    private Method method;

    public DynamicHandler(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.method.invoke(target, args);
    }
}
