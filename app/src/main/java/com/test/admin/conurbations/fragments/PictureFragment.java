package com.test.admin.conurbations.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentPrettyFragmentPagerAdapter;
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.annotations.events.OnClick;
import com.test.admin.conurbations.model.response.Moment;

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
    private Context mContext;
    private FragmentPrettyFragmentPagerAdapter fragmentPrettyFragmentPagerAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    protected void initData(Bundle bundle) {
        initAppBarSetting();

        String[] mTitles = mContext.getResources().getStringArray(R.array.sougou_tab);
        Fragment[] mFragments = new Fragment[mTitles.length];
        mFragments[0] = new PrettyPicturesListFragmentList();
        ((PrettyPicturesListFragmentList) mFragments[0]).setRange(Moment.SGImgType.美女);
        for (int i = 1; i < mTitles.length; i++) {
            mFragments[i] = new SouGouImageFragment();
            ((SouGouImageFragment) mFragments[i]).setRange(mTitles[i]);
        }

        fragmentPrettyFragmentPagerAdapter = new FragmentPrettyFragmentPagerAdapter(getChildFragmentManager(), mTitles, mFragments);
        mContentViewPager.setAdapter(fragmentPrettyFragmentPagerAdapter);
        mContentViewPager.setOffscreenPageLimit(5);
        mHeadTabLayout.setupWithViewPager(mContentViewPager);
        mHeadTabLayout.setBackgroundColor(getArguments().getInt("content"));
    }

    public void initAppBarSetting() {
        mHeadAppBarLayout.addOnOffsetChangedListener((appBarLayout, i) -> {
            if (i != 0) {
                mViewFloatingActionButton.hide();
            } else {
                mViewFloatingActionButton.show();
            }
        });
    }

    @OnClick("mViewFloatingActionButton")
    public void clickFab(View view) {
        ((BaseLazyListFragment) fragmentPrettyFragmentPagerAdapter.getFragment(mContentViewPager.getCurrentItem())).getRecyclerView().setSelection(0);
    }

    @Override
    public void detachView() {

    }
}
