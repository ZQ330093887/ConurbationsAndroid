package com.test.admin.conurbations.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by zhouqiong on 2017/1/8.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private String[] mTitles;
    private Fragment[] mFragments;

    public FragmentAdapter(FragmentManager fragmentManager, String[] title, Fragment[] fragments) {
        super(fragmentManager);
        this.mFragments = fragments;
        this.mTitles = title;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public Fragment getFragment(int position) {
        return mFragments[position];
    }
}
