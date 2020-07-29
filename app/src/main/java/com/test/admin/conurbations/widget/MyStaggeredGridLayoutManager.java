package com.test.admin.conurbations.widget;

import android.content.Context;

import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.adapter.BaseListAdapter;


/**
 * Created by Stay on 5/3/16.
 * Powered by www.stay4it.com
 */
public class MyStaggeredGridLayoutManager extends StaggeredGridLayoutManager implements ILayoutManager {

    public MyStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return this;
    }

    @Override
    public int findLastVisiblePosition() {
        int[] positions = null;
        positions = findLastVisibleItemPositions(positions);
        return positions[0];
    }

    @Override
    public void setUpAdapter(BaseListAdapter adapter) {

    }
}
