package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.MyRecyclerViewAdapter;
import com.test.admin.conurbations.data.entity.Moment;
import com.test.admin.conurbations.presenter.AllResourcePresenter;

public class Two extends BaseListFragment {
    private Moment.Range range;
    private AllResourcePresenter resourcePresenter;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter recyclerViewAdapter;

    public void setRange(Moment.Range range) {
        this.range = range;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_two, container, false);
        initData();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        //这里的数字决定了mRecyclerView中Item的条数
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        return view;
    }

    private void initData() {
        resourcePresenter = new AllResourcePresenter();
        resourcePresenter.getAllResourceData();
    }

    @Override
    protected void refreshList(int page) {

    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        return null;
    }

    @Override
    protected void setUpPresenter() {

    }
}
