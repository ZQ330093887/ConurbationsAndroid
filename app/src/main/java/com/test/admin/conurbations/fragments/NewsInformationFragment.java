package com.test.admin.conurbations.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.test.admin.conurbations.adapter.NewsInformationFragmentPagerAdapter;
import com.test.admin.conurbations.annotations.FindView;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class NewsInformationFragment extends BaseFragment {
    @FindView
    TabLayout mHeadTabLayout;
    @FindView
    ViewPager mContentViewPager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected void initData(Bundle bundle) {
        NewsInformationFragmentPagerAdapter mInformationFragmentPagerAdapter = new NewsInformationFragmentPagerAdapter(getContext(), getChildFragmentManager());
        mHeadTabLayout.setTabsFromPagerAdapter(mInformationFragmentPagerAdapter);
        mContentViewPager.setAdapter(mInformationFragmentPagerAdapter);
        mContentViewPager.setOffscreenPageLimit(5);
        mHeadTabLayout.setupWithViewPager(mContentViewPager);
        mHeadTabLayout.setBackgroundColor(getArguments().getInt("content"));
    }
}
