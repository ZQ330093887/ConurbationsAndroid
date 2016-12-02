package com.test.admin.conurbations.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.MyRecyclerViewAdapter;
import com.test.admin.conurbations.models.VO.Moment;
import com.test.admin.conurbations.presenter.WelfarePresenter;

import java.util.ArrayList;
import java.util.List;

public class FragmentOne extends BaseFragment {
    private Moment.Range range;
    WelfarePresenter welfarePresenter;

    @Override
    public BaseFragment newInstance() {
        return new FragmentOne();
    }

    public void setRange(Moment.Range range) {
        this.range = range;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_one, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add(i);
        }
        mRecyclerView.setAdapter(new MyRecyclerViewAdapter(getActivity(), datas, range));
        welfarePresenter = new WelfarePresenter();
        welfarePresenter.getWelfareData();
        return view;
    }


}
