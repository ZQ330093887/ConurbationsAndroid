package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.FragmentVideoBinding;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;

/**
 * Created by zhouqiong on 2019/4/2.
 */
public class VideoFragment extends BaseFragment<FragmentVideoBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initData(Bundle bundle) {
        RxBus.getDefault().post(new Event(R.color.color_512DA8, Constants.STATUE_BAR_COLOR));
        String[] mTitles = getActivity().getResources().getStringArray(R.array.video_tab);
        String[] mChannelCodes = getActivity().getResources().getStringArray(R.array.channel_code_video);
        Fragment[] mFragments = new Fragment[mTitles.length];

        mFragments[0] = new VideoIndexFragment();//抖音

        for (int i = 1; i < mTitles.length; i++) {
            mFragments[i] = new VideoListFragment();//头条视频
            ((VideoListFragment) mFragments[i]).setRange(mChannelCodes[i]);
        }

        FragmentAdapter videoFragmentPagerAdapter = new FragmentAdapter(getChildFragmentManager(), mTitles, mFragments);
        mBinding.get().vpNbView.setAdapter(videoFragmentPagerAdapter);
        mBinding.get().tlNbView.setupWithViewPager(mBinding.get().vpNbView);
        mBinding.get().vpNbView.setOffscreenPageLimit(10);
        mBinding.get().tlNbView.setBackgroundColor(getArguments().getInt("content"));
    }

    @Override
    public void detachView() {

    }
}
