package com.test.admin.conurbations.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.fragments.PrettyPicturesListFragmentList;
import com.test.admin.conurbations.fragments.WelfareFragment;

/**
 * Created by waly6 on 2015/10/8.
 */
public class ContentFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"精选", "竞技", "热榜" ,"录像"};


    private Fragment[] mFragments = new Fragment[4];

    public ContentFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        mFragments[0] = new WelfareFragment();
        ((WelfareFragment) mFragments[0]).setRange(Moment.Range.ONE);
        mFragments[1] = new PrettyPicturesListFragmentList();
        ((PrettyPicturesListFragmentList) mFragments[1]).setRange(Moment.SGImgType.每日一笑);
        mFragments[2] = new PrettyPicturesListFragmentList();
        ((PrettyPicturesListFragmentList) mFragments[2]).setRange(Moment.SGImgType.每日一笑);
        mFragments[3] = new PrettyPicturesListFragmentList();
        ((PrettyPicturesListFragmentList) mFragments[3]).setRange(Moment.SGImgType.每日一笑);
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
