package com.test.admin.conurbations.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.SouGouImageFragmentPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouqiong on 2015/9/23.
 */
public class FragmentSGImage extends BaseFragment {

    @Bind(R.id.tabLayout_index)
    TabLayout tabLayoutIndex;
    @Bind(R.id.viewpager_index)
    ViewPager viewpagerIndex;
    private Context mContext;
    private SouGouImageFragmentPagerAdapter souGouImageFragmentPagerAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public BaseFragment newInstance() {
        return new FragmentSGImage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.index_fragment, container, false);
        int content = getArguments().getInt("content");
        ButterKnife.bind(this, viewRoot);
        mContext.getResources();
        souGouImageFragmentPagerAdapter = new SouGouImageFragmentPagerAdapter(mContext,getChildFragmentManager());
        tabLayoutIndex.setTabsFromPagerAdapter(souGouImageFragmentPagerAdapter);
        viewpagerIndex.setAdapter(souGouImageFragmentPagerAdapter);
        viewpagerIndex.setOffscreenPageLimit(5);
        tabLayoutIndex.setupWithViewPager(viewpagerIndex);
        tabLayoutIndex.setBackgroundColor(content);
        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
