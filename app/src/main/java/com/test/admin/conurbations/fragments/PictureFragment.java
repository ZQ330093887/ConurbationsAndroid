package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.test.admin.conurbations.adapter.FragmentPrettyFragmentPagerAdapter;
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.annotations.events.OnClick;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class PictureFragment extends BaseFragment {

    @FindView
    TabLayout mHeadTabLayout;
    @FindView
    ViewPager mContentViewPager;
    @FindView
    FloatingActionButton mViewFloatingActionButton;
    @FindView
    AppBarLayout mHeadAppBarLayout;
    private FragmentPrettyFragmentPagerAdapter fragmentPrettyFragmentPagerAdapter;

    @Override
    protected void initData(Bundle bundle) {
        initAppBarSetting();
        fragmentPrettyFragmentPagerAdapter = new FragmentPrettyFragmentPagerAdapter(getContext(), getChildFragmentManager());
        mHeadTabLayout.setTabsFromPagerAdapter(fragmentPrettyFragmentPagerAdapter);
        mContentViewPager.setAdapter(fragmentPrettyFragmentPagerAdapter);
        mContentViewPager.setOffscreenPageLimit(5);
        mHeadTabLayout.setupWithViewPager(mContentViewPager);
        mHeadTabLayout.setBackgroundColor(getArguments().getInt("content"));
    }

    public void initAppBarSetting() {
        mHeadAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i != 0) {
                    mViewFloatingActionButton.hide();
                } else {
                    mViewFloatingActionButton.show();
                }
            }
        });
    }

    @OnClick("mViewFloatingActionButton")
    public void clickFab(View view) {
        ((BaseLazyListFragment) fragmentPrettyFragmentPagerAdapter.getFragment(mContentViewPager.getCurrentItem())).getRecyclerView().setSelection(0);
    }
}
