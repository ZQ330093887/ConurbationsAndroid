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
import com.test.admin.conurbations.adapter.SouGouImageFragmentPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class FragmentPrettyPictures extends BaseFragment {

    @Bind(R.id.tabLayout_index)
    TabLayout tabLayoutIndex;
    @Bind(R.id.viewpager_index)
    ViewPager viewpagerIndex;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;
    private Context mContext;
    private SouGouImageFragmentPagerAdapter souGouImageFragmentPagerAdapter;

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
        souGouImageFragmentPagerAdapter = new SouGouImageFragmentPagerAdapter(mContext, getChildFragmentManager());
        tabLayoutIndex.setTabsFromPagerAdapter(souGouImageFragmentPagerAdapter);
        viewpagerIndex.setAdapter(souGouImageFragmentPagerAdapter);
        viewpagerIndex.setOffscreenPageLimit(5);
        tabLayoutIndex.setupWithViewPager(viewpagerIndex);
        tabLayoutIndex.setBackgroundColor(content);
        return viewRoot;
    }

    public void initAppBarSetting() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i != 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
    }

    @OnClick(R.id.fab)
    public void clickFab(View view) {
        ((BaseListFragment) souGouImageFragmentPagerAdapter.getFragment(viewpagerIndex.getCurrentItem())).getRecyclerView().setSelection(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
