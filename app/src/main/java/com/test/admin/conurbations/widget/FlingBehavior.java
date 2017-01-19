package com.test.admin.conurbations.widget;

/**
 * Created by zhouqiong on 2017/1/19.
 */

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lijinshan on 2017/1/15.
 */

public final class FlingBehavior extends AppBarLayout.Behavior {
    private Map<RecyclerView, RecyclerViewScrollListener> scrollListenerMap = new HashMap<>(); //keep scroll listener map, the custom scroll listener also keep the current scroll Y position.

    public FlingBehavior() {
    }

    public FlingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param coordinatorLayout
     * @param child             The child that attached the behavior (AppBarLayout)
     * @param target            The scrolling target e.g. a recyclerView or NestedScrollView
     * @param velocityX
     * @param velocityY
     * @param consumed          The fling should be consumed by the scrolling target or not
     * @return
     */
    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        if (target instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) target;
            if (scrollListenerMap.get(recyclerView) == null) {
                RecyclerViewScrollListener recyclerViewScrollListener = new RecyclerViewScrollListener(coordinatorLayout, child, this);
                scrollListenerMap.put(recyclerView, recyclerViewScrollListener);
                recyclerView.addOnScrollListener(recyclerViewScrollListener);
            }
            scrollListenerMap.get(recyclerView).setVelocity(velocityY * 0.3f);
            consumed = scrollListenerMap.get(recyclerView).getScrolledY() > 0; //recyclerView only consume the fling when it's not scrolled to the top
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    private static class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        private int scrolledY;
        private boolean dragging;
        private float velocity;
        private WeakReference<CoordinatorLayout> coordinatorLayoutRef;
        private WeakReference<AppBarLayout> childRef;
        private WeakReference<FlingBehavior> behaviorWeakReference;

        public RecyclerViewScrollListener(CoordinatorLayout coordinatorLayout, AppBarLayout child, FlingBehavior barBehavior) {
            coordinatorLayoutRef = new WeakReference<CoordinatorLayout>(coordinatorLayout);
            childRef = new WeakReference<AppBarLayout>(child);
            behaviorWeakReference = new WeakReference<FlingBehavior>(barBehavior);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            dragging = newState == RecyclerView.SCROLL_STATE_DRAGGING;
        }

        public void setVelocity(float velocity) {
            this.velocity = velocity;
        }

        public int getScrolledY() {
            return scrolledY;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            scrolledY += dy;

            if (scrolledY <= 0 && !dragging && childRef.get() != null && coordinatorLayoutRef.get() != null && behaviorWeakReference.get() != null) {
                //manually trigger the fling when it's scrolled at the top
                behaviorWeakReference.get().onNestedFling(coordinatorLayoutRef.get(), childRef.get(), recyclerView, 0, velocity, false);
            }
        }
    }
}
