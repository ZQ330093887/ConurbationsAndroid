package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.test.admin.conurbations.adapter.NBAFragmentPagerAdapter;
import com.test.admin.conurbations.annotations.FindView;

/**
 * Created by zhouqiong on 2017/4/13.
 */
public class NBAFragment extends BaseFragment {
    @FindView
    TabLayout mViewTabLayout;
    @FindView
    ViewPager mViewViewPager;

    @Override
    protected void initData(Bundle bundle) {
        NBAFragmentPagerAdapter NBAFragmentPagerAdapter = new NBAFragmentPagerAdapter(getContext(), getChildFragmentManager());
        mViewViewPager.setAdapter(NBAFragmentPagerAdapter);
        mViewViewPager.setOffscreenPageLimit(4);
        mViewTabLayout.setupWithViewPager(mViewViewPager);
        mViewTabLayout.setBackgroundColor(getArguments().getInt("content"));
    }
}
