package com.test.admin.conurbations.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.test.admin.conurbations.R;
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
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    protected void initData(Bundle bundle) {
        String[] mTitles = mContext.getResources().getStringArray(R.array.news_info_tab);
        String[] mTitlesId = mContext.getResources().getStringArray(R.array.news_info_tab_id);
        Fragment[] mFragments = new Fragment[mTitles.length];
        mFragments[0] = new NewsInfoIndexFragment();
        ((NewsInfoIndexFragment) mFragments[0]).setRange("知乎");
        for (int i = 1; i < mTitles.length; i++) {
            mFragments[i] = new NewsInfoListFragment();
            ((NewsInfoListFragment) mFragments[i]).setTable(mTitlesId[i]);
        }

        NewsInformationFragmentPagerAdapter mInformationFragmentPagerAdapter = new NewsInformationFragmentPagerAdapter(getChildFragmentManager(), mTitles, mFragments);
        mContentViewPager.setAdapter(mInformationFragmentPagerAdapter);
        mContentViewPager.setOffscreenPageLimit(5);
        mHeadTabLayout.setupWithViewPager(mContentViewPager);
        mHeadTabLayout.setBackgroundColor(getArguments().getInt("content"));
    }

    @Override
    public void detachView() {

    }
}
