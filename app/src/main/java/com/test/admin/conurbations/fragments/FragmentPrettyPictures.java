package com.test.admin.conurbations.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentPrettyFragmentPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class FragmentPrettyPictures extends BaseFragment {

    @Bind(R.id.tb_pretty_pictures_hand)
    TabLayout mHeadTabLayout;
    @Bind(R.id.vp_pretty_pictures_content)
    ViewPager mContentViewPager;
    @Bind(R.id.fab_pretty_pictures_fab)
    FloatingActionButton mFloatingActionButton;
    @Bind(R.id.abl_pretty_pictures_hand)
    AppBarLayout mHeadAppBarLayout;
    private Context mContext;
    private FragmentPrettyFragmentPagerAdapter fragmentPrettyFragmentPagerAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public BaseFragment newInstance() {
        return new FragmentPrettyPictures();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.pretty_pictures_fragment, container, false);
        int content = getArguments().getInt("content");
        ButterKnife.bind(this, viewRoot);
        initAppBarSetting();
        mContext.getResources();
        fragmentPrettyFragmentPagerAdapter = new FragmentPrettyFragmentPagerAdapter(mContext, getChildFragmentManager());
        mHeadTabLayout.setTabsFromPagerAdapter(fragmentPrettyFragmentPagerAdapter);
        mContentViewPager.setAdapter(fragmentPrettyFragmentPagerAdapter);
        mContentViewPager.setOffscreenPageLimit(5);
        mHeadTabLayout.setupWithViewPager(mContentViewPager);
        mHeadTabLayout.setBackgroundColor(content);
        return viewRoot;
    }

    public void initAppBarSetting() {
        mHeadAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i != 0) {
                    mFloatingActionButton.hide();
                } else {
                    mFloatingActionButton.show();
                }
            }
        });
    }

    @OnClick(R.id.fab_pretty_pictures_fab)
    public void clickFab(View view) {
        ((BaseListFragment) fragmentPrettyFragmentPagerAdapter.getFragment(mContentViewPager.getCurrentItem())).getRecyclerView().setSelection(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
