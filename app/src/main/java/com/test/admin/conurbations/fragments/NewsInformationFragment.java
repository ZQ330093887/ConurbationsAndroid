package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentAdapter;
import com.test.admin.conurbations.databinding.FragmentNewsInformationBinding;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.EventType;
import com.test.admin.conurbations.rxbus.RxBus;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class NewsInformationFragment extends BaseFragment<FragmentNewsInformationBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_information;
    }

    @Override
    protected void initData(Bundle bundle) {
        RxBus.getDefault().post(new Event(R.color.colorBluePrimary, EventType.STATUE_BAR_COLOR));
        String[] mTitles = getActivity().getResources().getStringArray(R.array.news_info_tab);
        String[] mTitlesId = getActivity().getResources().getStringArray(R.array.news_info_tab_id);
        Fragment[] mFragments = new Fragment[mTitles.length];
        mFragments[0] = new NewsInfoIndexFragment();
        ((NewsInfoIndexFragment) mFragments[0]).setRange("知乎");
        for (int i = 1; i < mTitles.length; i++) {
            mFragments[i] = new NewsInfoListFragment();
            ((NewsInfoListFragment) mFragments[i]).setTable(mTitlesId[i]);
        }

        FragmentAdapter mInformationFragmentPagerAdapter = new FragmentAdapter(getChildFragmentManager(), mTitles, mFragments);
        mBinding.get().vpNewsInformationContent.setAdapter(mInformationFragmentPagerAdapter);
        mBinding.get().vpNewsInformationContent.setOffscreenPageLimit(5);
        mBinding.get().tlNewsInformationHead.setupWithViewPager(mBinding.get().vpNewsInformationContent);
        mBinding.get().tlNewsInformationHead.setBackgroundColor(getArguments().getInt("content"));
    }

    @Override
    public void detachView() {

    }
}
