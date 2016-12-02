package com.test.admin.conurbations.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.test.admin.conurbations.fragments.FragmentOne;
import com.test.admin.conurbations.models.VO.Moment;

/**
 * Created by waly6 on 2015/10/8.
 */
public class IndexFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] titles = new String[]{"推荐", "新闻", "专栏" ,"热榜" , "图集"};

    private Fragment[] fragments = new Fragment[5];

    public IndexFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        fragments[0] = new FragmentOne();
        ((FragmentOne) fragments[0]).setRange(Moment.Range.ONE);
        fragments[1] = new FragmentOne();
        ((FragmentOne) fragments[1]).setRange(Moment.Range.TWO);
        fragments[2] = new FragmentOne();
        ((FragmentOne) fragments[2]).setRange(Moment.Range.THREE);
        fragments[3] = new FragmentOne();
        ((FragmentOne) fragments[3]).setRange(Moment.Range.FOUR);
        fragments[4] = new FragmentOne();
        ((FragmentOne) fragments[4]).setRange(Moment.Range.FIVE);
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
