package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.FragmentNbBinding;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;

/**
 * Created by zhouqiong on 2017/4/13.
 */
public class NBAFragment extends BaseFragment<FragmentNbBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nb;
    }

    @Override
    protected void initData(Bundle bundle) {
        RxBus.getDefault().post(new Event(R.color.colorDeepPrimary, Constants.STATUE_BAR_COLOR));
        String[] mTitles = getActivity().getResources().getStringArray(R.array.nba_tab);
        String[] mTitlesId = getActivity().getResources().getStringArray(R.array.nba_tab_id);
        Fragment[] mFragments = new Fragment[mTitles.length];
        for (int i = 0; i < mTitles.length; i++) {
            mFragments[i] = new NbaIndexFragment();
            ((NbaIndexFragment) mFragments[i]).setType(mTitlesId[i]);
        }

        FragmentAdapter NBAFragmentPagerAdapter = new FragmentAdapter(getChildFragmentManager(), mTitles, mFragments);
        mBinding.get().vpNbView.setAdapter(NBAFragmentPagerAdapter);
        mBinding.get().vpNbView.setOffscreenPageLimit(4);
        mBinding.get().tlNbView.setupWithViewPager(mBinding.get().vpNbView);
        mBinding.get().tlNbView.setBackgroundColor(getArguments().getInt("content"));
    }

    @Override
    public void detachView() {

    }
}
