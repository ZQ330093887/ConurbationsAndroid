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
    public BaseFragment newInstance() {
        return new IndexFragment();
    }

    @Override
    protected void initData(Bundle bundle) {
        int content = getArguments().getInt("content");
        IndexFragmentPagerAdapter indexFragmentPagerAdapter = new IndexFragmentPagerAdapter(mContext, getChildFragmentManager());
        mHeadTabLayout.setTabsFromPagerAdapter(indexFragmentPagerAdapter);
        mContentViewPager.setAdapter(indexFragmentPagerAdapter);
        mContentViewPager.setOffscreenPageLimit(5);
        mHeadTabLayout.setupWithViewPager(mContentViewPager);
        mHeadTabLayout.setBackgroundColor(content);
    }
}
