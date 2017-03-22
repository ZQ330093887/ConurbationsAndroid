package com.test.admin.conurbations.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.NewsInformationFragmentPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by zhouqiong on 2016/9/23.
 */
public class FragmentNewsInformation extends BaseFragment {
    private Context mContext;
    @Bind(R.id.tb_news_info_head)
    TabLayout mHeadTabLayout;
    @Bind(R.id.vp_news_info_content)
    ViewPager mContentViewPager;
    NewsInformationFragmentPagerAdapter mInformationFragmentPagerAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public BaseFragment newInstance() {
        return new FragmentNewsInformation();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.news_information_fragment, container, false);
        int content = getArguments().getInt("content");
        ButterKnife.bind(this, viewRoot);
        mContext.getResources();
        mInformationFragmentPagerAdapter = new NewsInformationFragmentPagerAdapter(mContext, getChildFragmentManager());
        mHeadTabLayout.setTabsFromPagerAdapter(mInformationFragmentPagerAdapter);
        mContentViewPager.setAdapter(mInformationFragmentPagerAdapter);
        mContentViewPager.setOffscreenPageLimit(5);
        mHeadTabLayout.setupWithViewPager(mContentViewPager);
        mHeadTabLayout.setBackgroundColor(content);
        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
