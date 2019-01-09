package com.test.admin.conurbations.utils;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by ZQiong on 2018/12/26.
 */
public class RecyclerUtils {

    public static void initLinearLayoutVertical(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public static void initLinearLayoutHorizontal(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public static void initStaggered(RecyclerView recyclerView, int spanCount) {
        GridLayoutManager sgm = new GridLayoutManager(recyclerView.getContext(), spanCount);
        recyclerView.setLayoutManager(sgm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
