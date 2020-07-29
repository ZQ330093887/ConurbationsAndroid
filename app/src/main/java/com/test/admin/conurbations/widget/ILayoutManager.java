package com.test.admin.conurbations.widget;


import androidx.recyclerview.widget.RecyclerView;

import com.test.admin.conurbations.adapter.BaseListAdapter;


/**
 * Created by Stay on 5/3/16.
 * Powered by www.stay4it.com
 */
public interface ILayoutManager {
    RecyclerView.LayoutManager getLayoutManager();
    int findLastVisiblePosition();
    void setUpAdapter(BaseListAdapter adapter);
}
