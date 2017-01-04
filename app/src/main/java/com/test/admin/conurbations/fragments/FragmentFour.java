package com.test.admin.conurbations.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.data.entity.Moment;


public class FragmentFour extends Fragment {

    private Moment.Range range;

    public void setRange(Moment.Range range) {
        this.range = range;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_four, container, false);
        TextView mTextView = (TextView) view.findViewById(R.id.fragment_tv);
        mTextView.setText("这是第四个Fragment" + range);
        return view;
    }


}
