package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.FragmentTeam;
import com.test.admin.conurbations.fragments.NewsListFragment;

/**
 * Created by zhouqiong on 2017/1/8.
 */
public class NewsInformationFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private String[] titlesId;
    private Fragment[] fragments;

    public NewsInformationFragmentPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);

        titles = context.getResources().getStringArray(R.array.news_info_tab);
        titlesId = context.getResources().getStringArray(R.array.news_info_tab_id);
        fragments = new Fragment[titles.length];
        fragments[0] = new FragmentTeam();
        ((FragmentTeam) fragments[0]).setRange("知乎");
        for (int i = 1; i < titles.length; i++) {
            fragments[i] = new NewsListFragment();
            ((NewsListFragment) fragments[i]).setTable(titlesId[i]);
        }
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
