package com.test.admin.conurbations.utils;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * Created by ZQiong on 2018/9/25.
 */

public class AutoClearedValue<T> {
    private T value;

    public AutoClearedValue(final Fragment fragment, T value) {
        final FragmentManager fragmentManager = fragment.getFragmentManager();
        fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                if (f == fragment) {
                    AutoClearedValue.this.value = null;
                    fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                }

            }
        }, false);
        this.value = value;
    }

    public T get() {
        return this.value;
    }
}

