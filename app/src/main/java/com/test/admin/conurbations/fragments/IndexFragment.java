package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.IndexFragmentPagerAdapter;
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.model.response.Moment;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class IndexFragment extends BaseFragment {
    @FindView
    TabLayout mHeadTabLayout;
    @FindView
    ViewPager mContentViewPager;

    @Override
    protected void initData(Bundle bundle) {
        String[] mTitles = getApplicationContext().getResources().getStringArray(R.array.index_tab);
        Fragment[] mFragments = new Fragment[mTitles.length];
        mFragments[0] = new GankDayFragment();
        ((GankDayFragment) mFragments[0]).setRange(Moment.Range.THREE);
        mFragments[1] = new WelfareFragment();
        ((WelfareFragment) mFragments[1]).setRange(Moment.Range.ONE);
        for (int i = 2; i < mTitles.length; i++) {
            mFragments[i] = new GanHuoFragment();
            ((GanHuoFragment) mFragments[i]).setRange(mTitles[i]);
        }

        mContentViewPager.setAdapter(new IndexFragmentPagerAdapter(getChildFragmentManager(), mTitles, mFragments));
        mContentViewPager.setOffscreenPageLimit(9);
        mHeadTabLayout.setupWithViewPager(mContentViewPager);
        mHeadTabLayout.setBackgroundColor(getArguments().getInt("content"));
    }

    @Override
    public void detachView() {

    }
}
