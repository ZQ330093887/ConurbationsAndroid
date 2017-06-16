package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.NbaIndexFragment;

/**
 * Created by zhouqiong on 2017/1/8.
 */
public class NBAFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private String[] mTitles;
    private String[] mTitlesId;
    private Fragment[] mFragments;

    public NBAFragmentPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mTitles =context.getResources().getStringArray(R.array.nba_tab);
        mTitlesId =context.getResources().getStringArray(R.array.nba_tab_id);
        mFragments = new Fragment[mTitles.length];
        for (int i = 0; i < mTitles.length; i++) {
            mFragments[i] = new NbaIndexFragment();
            ((NbaIndexFragment) mFragments[i]).setType(mTitlesId[i]);
        }
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
}
