package com.test.admin.conurbations.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.test.admin.conurbations.data.entity.Moment;
import com.test.admin.conurbations.fragments.FragmentFour;

/**
 * Created by waly6 on 2015/10/8.
 */
public class FriendsFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] titles = new String[]{"重要", "已结束", "未结束" ,"关注" , "数据"};

    private Fragment[] fragments = new Fragment[5];

    public FriendsFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        fragments[0] = new FragmentFour();
        ((FragmentFour) fragments[0]).setRange(Moment.Range.ONE);
        fragments[1] = new FragmentFour();
        ((FragmentFour) fragments[1]).setRange(Moment.Range.TWO);
        fragments[2] = new FragmentFour();
        ((FragmentFour) fragments[2]).setRange(Moment.Range.THREE);
        fragments[3] = new FragmentFour();
        ((FragmentFour) fragments[3]).setRange(Moment.Range.FOUR);
        fragments[4] = new FragmentFour();
        ((FragmentFour) fragments[4]).setRange(Moment.Range.FIVE);
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
