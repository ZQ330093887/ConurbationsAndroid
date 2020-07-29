package com.test.admin.conurbations.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentAdapter;
import com.test.admin.conurbations.databinding.FragmentNewsInformationBinding;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class MVFragment extends BaseFragment<FragmentNewsInformationBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_information;
    }

    public static MVFragment newInstance() {
        Bundle args = new Bundle();
        MVFragment fragment = new MVFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData(Bundle bundle) {
        String[] mTitles = getActivity().getResources().getStringArray(R.array.my_mv);
        Fragment[] mFragments = new Fragment[mTitles.length];

        mFragments[0] = new MvListFragment();//排行榜
        ((MvListFragment) mFragments[0]).setType("rank");

        mFragments[1] = new MvListFragment();//最近更新
        ((MvListFragment) mFragments[1]).setType("recently");


        FragmentAdapter mInformationFragmentPagerAdapter = new FragmentAdapter(getChildFragmentManager(), mTitles, mFragments);
        mBinding.get().vpNewsInformationContent.setAdapter(mInformationFragmentPagerAdapter);
        mBinding.get().tlNewsInformationHead.setupWithViewPager(mBinding.get().vpNewsInformationContent);
        mBinding.get().tlNewsInformationHead.setBackgroundColor(getResources().getColor(R.color.white));
        mBinding.get().tlNewsInformationHead.setTabTextColors(getResources().getColor(R.color.colorBluePrimary), getResources().getColor(R.color.black));
    }

    @Override
    public void detachView() {
    }
}
