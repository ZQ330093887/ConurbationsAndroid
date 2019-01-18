package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.FragmentNewsInformationBinding;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class NewsInformationFragment extends BaseFragment<FragmentNewsInformationBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_information;
    }

    public static NewsInformationFragment newInstance(int color) {
        Bundle args = new Bundle();
        args.putInt("content", color);
        NewsInformationFragment fragment = new NewsInformationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData(Bundle bundle) {
        RxBus.getDefault().post(new Event(R.color.colorBluePrimary, Constants.STATUE_BAR_COLOR));
        String[] mTitles = getActivity().getResources().getStringArray(R.array.news_info_tab);
        Fragment[] mFragments = new Fragment[mTitles.length];

        mFragments[0] = MusicIndexFragment.newInstance();//我的

        mFragments[1] = DiscoverFragment.newInstance();//发现

        mFragments[2] = ChartsFragment.newInstance();//排行榜

        mFragments[3] = MVFragment.newInstance();//MV
        FragmentAdapter mInformationFragmentPagerAdapter = new FragmentAdapter(getChildFragmentManager(), mTitles, mFragments);
        mBinding.get().vpNewsInformationContent.setAdapter(mInformationFragmentPagerAdapter);
        mBinding.get().vpNewsInformationContent.setOffscreenPageLimit(3);
        mBinding.get().tlNewsInformationHead.setupWithViewPager(mBinding.get().vpNewsInformationContent);
        mBinding.get().tlNewsInformationHead.setBackgroundColor(getArguments().getInt("content"));
    }

    @Override
    public void detachView() {

    }
}
