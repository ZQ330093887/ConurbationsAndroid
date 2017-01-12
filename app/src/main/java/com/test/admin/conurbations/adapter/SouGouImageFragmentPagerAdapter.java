package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.SouGouImageFragment;

/**
 * Created by zhouqiong on 2017/1/12.
 */
public class SouGouImageFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private Fragment[] fragments;
    public SouGouImageFragmentPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        titles = context.getResources().getStringArray(R.array.sougou_tab);
        fragments = new Fragment[titles.length];
        for (int i = 0; i < titles.length; i++) {
            fragments[i] = new SouGouImageFragment();
            ((SouGouImageFragment) fragments[i]).setRange(titles[i]);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
