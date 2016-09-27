package com.test.admin.conurbations.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.test.admin.conurbations.fragments.FragmentFour;
import com.test.admin.conurbations.model.Moment;

/**
 * Created by zhouqiong on 2016/1/21.
 */
public class TeamFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] titles = new String[]{"推荐列表", "圈子内容"};

    private Fragment[] fragments = new Fragment[2];

    public TeamFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        fragments[0] = new FragmentFour();
        ((FragmentFour) fragments[0]).setRange(Moment.Range.ONE);
        fragments[1] = new FragmentFour();
        ((FragmentFour) fragments[1]).setRange(Moment.Range.TWO);
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
