package com.test.admin.conurbations.rxbus;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;

/**
 * Created by ZQiong on 2018/9/26.
 */

public class RxBus {
    private static volatile RxBus instance;
    private final Relay<Object> mBus = PublishRelay.create().toSerialized();

    public RxBus() {
    }

    public static RxBus getDefault() {
        if (instance == null) {
            Class var0 = RxBus.class;
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = RxBus.Holder.BUS;
                }
            }
        }

        return instance;
    }

    public void post(Object obj) {
        this.mBus.accept(obj);
    }

    public <T> Observable<T> toObservable(Class<T> tClass) {
        return this.mBus.ofType(tClass);
    }

    public Observable<Object> toObservable() {
        return this.mBus;
    }

    public boolean hasObservers() {
        return this.mBus.hasObservers();
    }

    private static class Holder {
        private static final RxBus BUS = new RxBus();

        private Holder() {
        }
    }
}
