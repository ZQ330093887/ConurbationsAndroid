package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.fragments.PrettyPicturesListFragmentList;
import com.test.admin.conurbations.fragments.SouGouImageFragment;

/**
 * Created by zhouqiong on 2017/1/12.
 */
public class FragmentPrettyFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles;
    private Fragment[] mFragments;

    public FragmentPrettyFragmentPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mTitles = context.getResources().getStringArray(R.array.sougou_tab);
        mFragments = new Fragment[mTitles.length];
        mFragments[0] = new PrettyPicturesListFragmentList();
        ((PrettyPicturesListFragmentList) mFragments[0]).setRange(Moment.SGImgType.美女);
        for (int i = 1; i < mTitles.length; i++) {
            mFragments[i] = new SouGouImageFragment();
            ((SouGouImageFragment) mFragments[i]).setRange(mTitles[i]);
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

    public Fragment getFragment(int position) {
        return mFragments[position];
    }
}
