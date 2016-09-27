package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.TeamFragmentPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouqiong on 2015/9/23.
 */
public class FragmentTeam extends BaseFragment {

    @Bind(R.id.tabLayout_team)
    TabLayout tabLayoutTeam;
    @Bind(R.id.viewpager_team)
    ViewPager viewpagerTeam;

    private TeamFragmentPagerAdapter pagerAdapter;

    @Override
    public BaseFragment newInstance() {
        return new FragmentTeam();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teamfragment, null);
        int content = getArguments().getInt("content");
        ButterKnife.bind(this, view);
        pagerAdapter = new TeamFragmentPagerAdapter(getChildFragmentManager());
        viewpagerTeam.setAdapter(pagerAdapter);
        viewpagerTeam.setOffscreenPageLimit(2);
        tabLayoutTeam.setupWithViewPager(viewpagerTeam);
        tabLayoutTeam.setTabMode(TabLayout.MODE_FIXED);
        tabLayoutTeam.setBackgroundColor(content);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
