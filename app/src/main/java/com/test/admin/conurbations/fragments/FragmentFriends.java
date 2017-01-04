package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FriendsFragmentPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouqiong on 2015/9/23.
 */
public class FragmentFriends extends BaseFragment {

    @Bind(R.id.tabLayout_friend)
    TabLayout tabLayoutFriend;
    @Bind(R.id.viewpager_friend)
    ViewPager viewpagerFriend;

    public BaseFragment newInstance(){
        return new FragmentFriends();
    }

    private FriendsFragmentPagerAdapter friendsFragmentPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendfragment, container, false);
        ButterKnife.bind(this, view);
        int content = getArguments().getInt("content");
        friendsFragmentPagerAdapter = new FriendsFragmentPagerAdapter(getChildFragmentManager());
        tabLayoutFriend.setTabsFromPagerAdapter(friendsFragmentPagerAdapter);
        viewpagerFriend.setAdapter(friendsFragmentPagerAdapter);
        viewpagerFriend.setOffscreenPageLimit(5);
        tabLayoutFriend.setupWithViewPager(viewpagerFriend);
        tabLayoutFriend.setBackgroundColor(content);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
