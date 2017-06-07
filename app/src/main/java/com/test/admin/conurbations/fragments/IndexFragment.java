package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.test.admin.conurbations.adapter.IndexFragmentPagerAdapter;
import com.test.admin.conurbations.annotations.FindView;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class IndexFragment extends BaseFragment {
    @FindView
    TabLayout mHeadTabLayout;
    @FindView
    ViewPager mContentViewPager;

    @Override
    protected void initData(Bundle bundle) {
        IndexFragmentPagerAdapter indexFragmentPagerAdapter = new IndexFragmentPagerAdapter(getContext(), getChildFragmentManager());
        mHeadTabLayout.setTabsFromPagerAdapter(indexFragmentPagerAdapter);
        mContentViewPager.setAdapter(indexFragmentPagerAdapter);
        mContentViewPager.setOffscreenPageLimit(5);
        mHeadTabLayout.setupWithViewPager(mContentViewPager);
        mHeadTabLayout.setBackgroundColor(getArguments().getInt("content"));
    }
}
