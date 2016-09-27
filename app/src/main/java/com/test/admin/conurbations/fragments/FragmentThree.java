package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.TextRecyclerViewAdapter;
import com.test.admin.conurbations.model.Moment;

import java.util.ArrayList;
import java.util.List;

public class FragmentThree extends Fragment {

    private Moment.Range range;
    private RecyclerView mRecyclerView;

    public void setRange(Moment.Range range) {
        this.range = range;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_three, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_three);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add(i);
        }
        mRecyclerView.setAdapter(new TextRecyclerViewAdapter(getActivity(), datas, range));
        return view;


    }

}
