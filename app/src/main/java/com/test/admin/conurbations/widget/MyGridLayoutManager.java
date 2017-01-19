package com.test.admin.conurbations.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.test.admin.conurbations.adapter.BaseListAdapter;


/**
 * Created by Stay on 5/3/16.
 * Powered by www.stay4it.com
 */
public class MyGridLayoutManager extends GridLayoutManager implements ILayoutManager {
    public MyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public MyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return this;
    }

    @Override
    public int findLastVisiblePosition() {
        return 0;
    }

    @Override
    public void setUpAdapter(BaseListAdapter adapter) {

    }
}
