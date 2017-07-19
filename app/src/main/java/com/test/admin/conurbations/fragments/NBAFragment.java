package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.test.admin.conurbations.R;
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
        String[] mTitles = getApplicationContext().getResources().getStringArray(R.array.nba_tab);
        String[] mTitlesId = getApplicationContext().getResources().getStringArray(R.array.nba_tab_id);
        Fragment[] mFragments = new Fragment[mTitles.length];
        for (int i = 0; i < mTitles.length; i++) {
            mFragments[i] = new NbaIndexFragment();
            ((NbaIndexFragment) mFragments[i]).setType(mTitlesId[i]);
        }

        NBAFragmentPagerAdapter NBAFragmentPagerAdapter = new NBAFragmentPagerAdapter(getChildFragmentManager(), mTitles, mFragments);
        mViewViewPager.setAdapter(NBAFragmentPagerAdapter);
        mViewViewPager.setOffscreenPageLimit(4);
        mViewTabLayout.setupWithViewPager(mViewViewPager);
        mViewTabLayout.setBackgroundColor(getArguments().getInt("content"));
    }

    @Override
    public void detachView() {

    }
}
