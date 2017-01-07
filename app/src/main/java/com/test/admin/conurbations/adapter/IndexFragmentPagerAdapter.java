package com.test.admin.conurbations.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.test.admin.conurbations.data.entity.Moment;
import com.test.admin.conurbations.fragments.GanHuoListFragment;
import com.test.admin.conurbations.fragments.TodayNewsFragment;
import com.test.admin.conurbations.fragments.WelfareFragment;

/**
 * Created by waly6 on 2015/10/8.
 */
public class IndexFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] titles = new String[]{"最新", "ALL", "App", "福利", "Android", "iOS", "前端", "瞎推荐", "拓展资源", "休息视频"};

    private Fragment[] fragments = new Fragment[10];

    public IndexFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments[0] = new TodayNewsFragment();
        ((TodayNewsFragment) fragments[0]).setRange(Moment.Range.THREE);
        fragments[1] = new GanHuoListFragment();
        ((GanHuoListFragment) fragments[1]).setRange(Moment.Type.all);
        fragments[2] = new GanHuoListFragment();
        ((GanHuoListFragment) fragments[2]).setRange(Moment.Type.App);
        fragments[3] = new WelfareFragment();
        ((WelfareFragment) fragments[3]).setRange(Moment.Range.ONE);
        fragments[4] = new GanHuoListFragment();
        ((GanHuoListFragment) fragments[4]).setRange(Moment.Type.Android);
        fragments[5] = new GanHuoListFragment();
        ((GanHuoListFragment) fragments[5]).setRange(Moment.Type.iOS);
        fragments[6] = new GanHuoListFragment();
        ((GanHuoListFragment) fragments[6]).setRange(Moment.Type.前端);
        fragments[7] = new GanHuoListFragment();
        ((GanHuoListFragment) fragments[7]).setRange(Moment.Type.瞎推荐);
        fragments[8] = new GanHuoListFragment();
        ((GanHuoListFragment) fragments[8]).setRange(Moment.Type.拓展资源);
        fragments[9] = new GanHuoListFragment();
        ((GanHuoListFragment) fragments[9]).setRange(Moment.Type.休息视频);
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
