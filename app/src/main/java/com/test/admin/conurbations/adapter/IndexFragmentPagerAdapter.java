package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.GanHuoFragment;
import com.test.admin.conurbations.fragments.GankDayFragment;
import com.test.admin.conurbations.fragments.WelfareFragment;
import com.test.admin.conurbations.model.response.Moment;

/**
 * Created by zhouqiong on 2017/1/8.
 */
public class IndexFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private String[] mTitles;
    private Fragment[] mFragments;
    public IndexFragmentPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);

        mTitles = context.getResources().getStringArray(R.array.index_tab);
        mFragments = new Fragment[mTitles.length];
        mFragments[0] = new GankDayFragment();
        ((GankDayFragment) mFragments[0]).setRange(Moment.Range.THREE);
        mFragments[1] = new WelfareFragment();
        ((WelfareFragment) mFragments[1]).setRange(Moment.Range.ONE);
        for (int i = 2; i < mTitles.length; i++) {
            mFragments[i] = new GanHuoFragment();
            ((GanHuoFragment) mFragments[i]).setRange(mTitles[i]);
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
