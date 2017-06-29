package com.test.admin.conurbations.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.test.admin.conurbations.adapter.IndexFragmentPagerAdapter;
import com.test.admin.conurbations.annotations.FindView;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class IndexFragment extends BaseFragment {
    private Context mContext;
    @FindView
    TabLayout mHeadTabLayout;
    @FindView
    ViewPager mContentViewPager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    protected void initData(Bundle bundle) {
        mContentViewPager.setAdapter(new IndexFragmentPagerAdapter(mContext, getChildFragmentManager()));
        mContentViewPager.setOffscreenPageLimit(9);
        mHeadTabLayout.setupWithViewPager(mContentViewPager);
        mHeadTabLayout.setBackgroundColor(getArguments().getInt("content"));
    }
}
